package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertView;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

// Not much we can do, orchestrating adverts is a lot of work.
@SuppressWarnings("PMD.GodClass")
public class NoPlayerAdsLoader implements AdsLoader, Player.EventListener, AdvertView.AdvertInteractionListener {

    private final AdvertsLoader loader;
    private final Handler handler;

    @Nullable
    private Player player;
    @Nullable
    private AdPlaybackState adPlaybackState;
    @Nullable
    private AdvertState advertState;
    @Nullable
    private EventListener eventListener;
    @Nullable
    private AdvertsLoader.Cancellable loadingAds;

    private NoPlayer.AdvertListener advertListener = NoOpAdvertListener.INSTANCE;
    private List<AdvertBreak> advertBreaks = Collections.emptyList();
    private boolean isPlayingAdvert;
    private int advertIndexInAdvertGroup = -1;
    private int advertGroupIndex = -1;
    private boolean advertsDisabled;
    private long advertBreakResumePosition;
    private long playbackResumePositionMillis;

    static NoPlayerAdsLoader create(AdvertsLoader loader) {
        return new NoPlayerAdsLoader(loader, new Handler(Looper.getMainLooper()));
    }

    NoPlayerAdsLoader(AdvertsLoader loader, Handler handler) {
        this.loader = loader;
        this.handler = handler;
    }

    public void bind(
            Optional<NoPlayer.AdvertListener> advertListener,
            long advertBreakResumePositionMillis,
            long playbackResumePositionMillis) {
        this.advertListener = advertListener.isPresent() ? advertListener.get() : NoOpAdvertListener.INSTANCE;
        this.advertBreakResumePosition = advertBreakResumePositionMillis;
        this.playbackResumePositionMillis = playbackResumePositionMillis;
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
            adPlaybackState = ResumeableAdverts.markAsResumeableFrom(
                    advertPlaybackState.adPlaybackState(),
                    advertBreakResumePosition,
                    playbackResumePositionMillis
            );

            handler.post(new Runnable() {
                @Override
                public void run() {
                    advertListener.onAdvertsLoaded(new ArrayList<>(advertBreaks));
                    updateAdPlaybackState();
                }
            });
        }

        @Override
        public void onAdvertsError(final Exception cause) {
            loadingAds = null;
            advertBreaks = Collections.emptyList();
            adPlaybackState = new AdPlaybackState();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateAdPlaybackState();
                    advertListener.onAdvertsLoadError(cause);
                }
            });
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
            updateAdPlaybackState();

            AdvertBreak advertBreak = advertBreaks.get(adGroupIndex);
            Advert advert = advertBreak.adverts().get(adIndexInAdGroup);
            advertListener.onAdvertPrepareError(advert, exception);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        if (reason == Player.TIMELINE_CHANGE_REASON_RESET || player == null || adPlaybackState == null || advertsDisabled) {
            // The player is being reset and this source will be released.
            return;
        }

        updateState();
    }

    private void updateState() {
        boolean wasPlayingAd = isPlayingAdvert;
        isPlayingAdvert = player.isPlayingAd();

        int previousAdvertGroupIndex = advertGroupIndex;
        int previousAdvertIndexInAdvertGroup = advertIndexInAdvertGroup;

        advertGroupIndex = isPlayingAdvert ? player.getCurrentAdGroupIndex() : C.INDEX_UNSET;
        advertIndexInAdvertGroup = isPlayingAdvert ? player.getCurrentAdIndexInAdGroup() : C.INDEX_UNSET;
        boolean advertFinished = wasPlayingAd && advertIndexInAdvertGroup != previousAdvertIndexInAdvertGroup;

        if (advertFinished) {
            adPlaybackState = adPlaybackState.withPlayedAd(previousAdvertGroupIndex, previousAdvertIndexInAdvertGroup);
            updateAdPlaybackState();
            notifyAdvertEnd(previousAdvertGroupIndex, previousAdvertIndexInAdvertGroup);
        }

        boolean advertStarted = isPlayingAdvert && advertIndexInAdvertGroup != previousAdvertIndexInAdvertGroup;
        if (advertStarted) {
            notifyAdvertStart(advertGroupIndex, advertIndexInAdvertGroup);
        }
    }

    private void notifyAdvertEnd(int playedAdvertGroupIndex, int playedAdvertIndexInAdvertGroup) {
        AdvertBreak advertBreak = advertBreaks.get(playedAdvertGroupIndex);
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertEnd(adverts.get(playedAdvertIndexInAdvertGroup));

        if (advertGroupIndex != playedAdvertGroupIndex) {
            advertListener.onAdvertBreakEnd(advertBreak);
            resetState();
        }
    }

    private void resetState() {
        advertGroupIndex = -1;
        advertIndexInAdvertGroup = -1;
        isPlayingAdvert = false;
    }

    private void notifyAdvertStart(int advertGroupIndex, int advertIndexInAdvertGroup) {
        AdvertBreak advertBreak = advertBreaks.get(advertGroupIndex);

        if (advertIndexInAdvertGroup == 0) {
            advertListener.onAdvertBreakStart(advertBreak);
        }

        Advert advert = advertBreak.adverts().get(advertIndexInAdvertGroup);
        advertListener.onAdvertStart(advert);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (player == null || adPlaybackState == null || advertsDisabled) {
            // We need all of the above to be able to respond to advert events.
            return;
        }

        updateState();
    }

    @Override
    public void onAdvertClicked() {
        if (isPlayingAdvert) {
            Advert advert = advertBreaks.get(advertGroupIndex).adverts().get(advertIndexInAdvertGroup);
            advertListener.onAdvertClicked(advert);
        }
    }

    void disableAdverts() {
        if (adPlaybackState == null || player == null || advertsDisabled) {
            return;
        }

        adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(advertBreaks, adPlaybackState);
        updateAdPlaybackState();
        advertListener.onAdvertsDisabled();
        resetState();
        advertsDisabled = true;
    }

    void skipAdvertBreak() {
        if (adPlaybackState == null || player == null || advertsDisabled || advertGroupIndex < 0) {
            return;
        }

        adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(advertGroupIndex, adPlaybackState);
        updateAdPlaybackState();
        advertListener.onAdvertBreakSkipped(advertBreaks.get(advertGroupIndex));
        resetState();
    }

    void skipAdvert() {
        if (adPlaybackState == null || player == null || advertsDisabled || advertIndexInAdvertGroup < 0 || advertGroupIndex < 0) {
            return;
        }

        adPlaybackState = SkippedAdverts.markAdvertAsSkipped(advertIndexInAdvertGroup, advertGroupIndex, adPlaybackState);
        updateAdPlaybackState();
        AdvertBreak advertBreak = advertBreaks.get(advertGroupIndex);
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertSkipped(adverts.get(advertIndexInAdvertGroup));
        if (advertIndexInAdvertGroup == adverts.size() - 1) {
            advertListener.onAdvertBreakEnd(advertBreak);
        }
        resetState();
    }

    void enableAdverts() {
        if (adPlaybackState == null || player == null || !advertsDisabled) {
            return;
        }
        resetState();

        AvailableAdverts.markSkippedAdvertsAsAvailable(advertBreaks, adPlaybackState);

        updateAdPlaybackState();
        advertListener.onAdvertsEnabled(advertBreaks);
        advertsDisabled = false;
    }
}
