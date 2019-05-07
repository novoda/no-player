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
import com.novoda.noplayer.AdvertView;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private EventListener eventListener;
    @Nullable
    private AdvertsLoader.Cancellable loadingAds;

    private NoPlayer.AdvertListener advertListener = NoOpAdvertListener.INSTANCE;
    private List<AdvertBreak> advertBreaks = Collections.emptyList();
    private int adIndexInGroup = -1;
    private int adGroupIndex = -1;
    private boolean advertsDisabled;

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

        if (reason == Player.TIMELINE_CHANGE_REASON_PREPARED) {
            long contentPosition = player.getContentPosition();
            if (contentPosition > 0) {
                PlayedAdverts playedAdverts = PlayedAdverts.from(contentPosition, advertBreaks, adPlaybackState);
                this.adPlaybackState = playedAdverts.adPlaybackState();
                updateAdPlaybackState();
                advertListener.onAdvertsSkipped(playedAdverts.playedAdvertBreaks());
                return;
            }
        }

        handleAdvertStart();
    }

    private void handleAdvertStart() {
        if (advertHasNotStarted()) {
            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();

            if (canPlayAdverts(adGroupIndex)) {
                notifyAdvertStart(advertBreaks.get(adGroupIndex));
            } else {
                resetAdvertPosition();
            }
        }
    }

    private boolean advertHasNotStarted() {
        return player.isPlayingAd() && (adGroupIndex == -1 || adIndexInGroup == -1);
    }

    private boolean canPlayAdverts(int adGroupIndex) {
        return isPlayingAdvert() && adPlaybackState.adGroups[adGroupIndex].hasUnplayedAds();
    }

    private void notifyAdvertStart(AdvertBreak advertBreak) {
        if (adIndexInGroup == 0) {
            advertListener.onAdvertBreakStart(advertBreak);
        }

        Advert advert = advertBreak.adverts().get(adIndexInGroup);
        advertListener.onAdvertStart(advert);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (player == null || adPlaybackState == null || advertsDisabled) {
            // We need all of the above to be able to respond to advert events.
            return;
        }

        if (reason == Player.DISCONTINUITY_REASON_SEEK) {
            long contentPosition = player.getContentPosition();
            AvailableAdverts.markAllFutureAdvertsAsAvailable(contentPosition, advertBreaks, adPlaybackState);
            updateAdPlaybackState();
            return;
        }

        if (reason == Player.DISCONTINUITY_REASON_AD_INSERTION) {
            if (isPlayingAdvert()) {
                notifyAdvertEnd(advertBreaks.get(adGroupIndex));
                adPlaybackState = adPlaybackState.withPlayedAd(adGroupIndex, adIndexInGroup);
                updateAdPlaybackState();
                resetAdvertPosition();
            }

            handleAdvertStart();
        }
    }

    private boolean isPlayingAdvert() {
        return adGroupIndex != -1 && adIndexInGroup != -1;
    }

    private void notifyAdvertEnd(AdvertBreak advertBreak) {
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertEnd(adverts.get(adIndexInGroup));

        if (adIndexInGroup == adverts.size() - 1) {
            advertListener.onAdvertBreakEnd(advertBreak);
        }
    }

    private void resetAdvertPosition() {
        adGroupIndex = -1;
        adIndexInGroup = -1;
    }

    @Override
    public void onAdvertClicked() {
        if (isPlayingAdvert()) {
            Advert advert = advertBreaks.get(adGroupIndex).adverts().get(adIndexInGroup);
            advertListener.onAdvertClicked(advert);
        }
    }

    void disableAdverts() {
        if (adPlaybackState == null || player == null) {
            return;
        }

        adPlaybackState = SkippedAdverts.markAllNonPlayedAdvertsAsSkipped(advertBreaks, adPlaybackState);
        updateAdPlaybackState();
        advertListener.onAdvertsDisabled();
        resetAdvertPosition();
        advertsDisabled = true;
    }

    void enableAdverts() {
        if (adPlaybackState == null || player == null) {
            return;
        }

        long contentPosition = player.getContentPosition();
        AvailableAdverts.markAllFutureAdvertsAsAvailable(contentPosition, advertBreaks, adPlaybackState);

        updateAdPlaybackState();
        advertListener.onAdvertsEnabled(advertBreaks);
        advertsDisabled = false;
    }
}
