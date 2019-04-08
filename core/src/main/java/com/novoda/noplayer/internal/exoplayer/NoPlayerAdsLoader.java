package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertBreakId;
import com.novoda.noplayer.AdvertId;
import com.novoda.noplayer.AdvertView;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NoPlayerAdsLoader implements AdsLoader, Player.EventListener, AdvertView.AdvertInteractionListener {

    private final AdvertsLoader loader;
    private final Handler handler;

    @Nullable
    private Player player;
    @Nullable
    private AdPlaybackState adPlaybackState;
    @Nullable
    private EventListener eventListener;
    @Nullable
    private AdvertsLoader.Cancellable loadingAds;

    private NoPlayer.AdvertListener advertListener = NoOpAdvertListener.INSTANCE;
    private AdvertView advertView = NoOpAdvertView.INSTANCE;
    private List<AdvertBreak> advertBreaks = Collections.emptyList();
    private int adIndexInGroup = -1;
    private int adGroupIndex = -1;

    static NoPlayerAdsLoader create(AdvertsLoader loader) {
        return new NoPlayerAdsLoader(loader, new Handler(Looper.getMainLooper()));
    }

    NoPlayerAdsLoader(AdvertsLoader loader, Handler handler) {
        this.loader = loader;
        this.handler = handler;
    }

    public void bind(Optional<NoPlayer.AdvertListener> advertListener) {
        this.advertListener = advertListener.isPresent() ? advertListener.get() : NoOpAdvertListener.INSTANCE;
    }

    void attach(AdvertView advertView) {
        this.advertView = advertView == null ? NoOpAdvertView.INSTANCE : new MainThreadAwareAdvertView(advertView, handler);
    }

    void detach(AdvertView advertView) { // TODO: Because we probably want to grab a listener from it.
        this.advertView = NoOpAdvertView.INSTANCE;
    }

    @Override
    public void setSupportedContentTypes(int... contentTypes) {
        // no-op
    }

    @Override
    public void start(final EventListener eventListener, AdViewProvider adViewProvider) {
        this.eventListener = eventListener;
        if (loadingAds != null) {
            return;
        }

        if (adPlaybackState == null) {
            loadingAds = loader.load(advertsLoadedCallback);
        } else {
            updateAdPlaybackState();
        }
    }

    private final AdvertsLoader.Callback advertsLoadedCallback = new AdvertsLoader.Callback() {
        @Override
        public void onAdvertsLoaded(List<AdvertBreak> breaks) {
            loadingAds = null;
            AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(breaks);
            advertBreaks = advertPlaybackState.advertBreaks();
            adPlaybackState = advertPlaybackState.adPlaybackState();
            advertView.setup(breaks, NoPlayerAdsLoader.this);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateAdPlaybackState();
                }
            });
        }

        @Override
        public void onAdvertsError(String message) {
            loadingAds = null;
            eventListener.onAdLoadError(null, null);
        }
    };

    private void updateAdPlaybackState() {
        if (eventListener != null) {
            eventListener.onAdPlaybackState(adPlaybackState);
        }
    }

    long advertDurationBy(int advertGroupIndex, int advertIndexInAdvertGroup) {
        if (advertGroupIndex >= advertBreaks.size()) {
            throw new IllegalStateException("Advert is being played but no data about advert breaks is cached.");
        }

        AdvertBreak advertBreak = advertBreaks.get(advertGroupIndex);
        if (advertIndexInAdvertGroup >= advertBreak.adverts().size()) {
            throw new IllegalStateException("Cached advert break data contains less adverts than current index.");
        }

        return C.msToUs(advertBreak.adverts().get(advertIndexInAdvertGroup).durationInMillis());
    }

    @Override
    public void stop() {
        if (loadingAds != null) {
            loadingAds.cancel();
            loadingAds = null;
        }
        if (adPlaybackState != null && player != null && player.isPlayingAd()) {
            adPlaybackState = adPlaybackState.withAdResumePositionUs(TimeUnit.MILLISECONDS.toMicros(player.getCurrentPosition()));
        }
        eventListener = null;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
        this.player.addListener(this);
    }

    @Override
    public void release() {
        if (player != null) {
            player.removeListener(this);
        }

        adPlaybackState = null;
        player = null;
    }

    @Override
    public void handlePrepareError(int adGroupIndex, int adIndexInAdGroup, IOException exception) {
        if (adPlaybackState != null) {
            adPlaybackState = adPlaybackState.withAdLoadError(adGroupIndex, adIndexInAdGroup);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        if (reason == Player.TIMELINE_CHANGE_REASON_RESET) {
            // The player is being reset and this source will be released.
            return;
        }

        if (player != null && adPlaybackState != null) {
            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();
            long contentPosition = player.getContentPosition();

            if (reason == Player.TIMELINE_CHANGE_REASON_PREPARED && isPlayingAdvert()) {
                if (contentPosition > 0) {
                    adPlaybackState = SkippedAdverts.from(contentPosition, advertBreaks, adPlaybackState);
                    updateAdPlaybackState();
                    return;
                }

                notifyAdvertStart(advertBreaks.get(adGroupIndex));
            }
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (reason == Player.DISCONTINUITY_REASON_AD_INSERTION && player != null && adPlaybackState != null) {
            if (isPlayingAdvert()) {
                notifyAdvertEnd(advertBreaks.get(adGroupIndex));
                adPlaybackState = adPlaybackState.withPlayedAd(adGroupIndex, adIndexInGroup);
                updateAdPlaybackState();
            }

            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();

            if (isPlayingAdvert()) {
                notifyAdvertStart(advertBreaks.get(adGroupIndex));
            }
        }
    }

    private boolean isPlayingAdvert() {
        return adGroupIndex != -1 && adIndexInGroup != -1;
    }

    private void notifyAdvertEnd(AdvertBreak advertBreak) {
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertEnd(adverts.get(adIndexInGroup).advertId());

        if (adIndexInGroup == adverts.size() - 1) {
            advertListener.onAdvertBreakEnd(advertBreak.advertBreakId());
        }
    }

    private void notifyAdvertStart(AdvertBreak advertBreak) {
        if (adIndexInGroup == 0) {
            advertListener.onAdvertBreakStart(advertBreak.advertBreakId());
        }

        Advert advert = advertBreak.adverts().get(adIndexInGroup);
        advertListener.onAdvertStart(advert.advertId());
    }

    @Override
    public void onAdvertClicked() {
        if (isPlayingAdvert()) {
            Advert advert = advertBreaks.get(adGroupIndex).adverts().get(adIndexInGroup);
            advertListener.onAdvertClicked(advert);
        }
    }

    private enum NoOpAdvertListener implements NoPlayer.AdvertListener {
        INSTANCE;

        @Override
        public void onAdvertBreakStart(AdvertBreakId advertBreakId) {
            // no-op
        }

        @Override
        public void onAdvertBreakEnd(AdvertBreakId advertBreakId) {
            // no-op
        }

        @Override
        public void onAdvertStart(AdvertId advertId) {
            // no-op
        }

        @Override
        public void onAdvertEnd(AdvertId advertId) {
            // no-op
        }

        @Override
        public void onAdvertClicked(Advert advert) {
            // no-op
        }
    }

    private enum NoOpAdvertView implements AdvertView {
        INSTANCE;

        @Override
        public void setup(List<AdvertBreak> advertBreaks, AdvertInteractionListener advertInteractionListener) {
            // no-op
        }

        @Override
        public void removeMarker(AdvertBreak advertBreak) {
            // no-op
        }
    }

    private class MainThreadAwareAdvertView implements AdvertView {

        private final AdvertView advertView;
        private final Handler handler;

        private MainThreadAwareAdvertView(AdvertView advertView, Handler handler) {
            this.advertView = advertView;
            this.handler = handler;
        }

        @Override
        public void setup(final List<AdvertBreak> advertBreaks, final AdvertInteractionListener advertInteractionListener) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    advertView.setup(new ArrayList<>(advertBreaks), advertInteractionListener);
                }
            });
        }

        @Override
        public void removeMarker(final AdvertBreak advertBreak) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    advertView.removeMarker(advertBreak); // TODO: Maybe we should create a copy?
                }
            });
        }
    }
}
