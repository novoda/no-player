package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;
import android.os.Looper;

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

import static com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_SEEK;

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
    @Nullable
    private AdvertState advertState;

    private NoPlayer.AdvertListener advertListener = NoOpAdvertListener.INSTANCE;
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
            final List<AdvertBreak> advertBreaks = advertPlaybackState.advertBreaks();
            adPlaybackState = ResumeableAdverts.markAsResumeableFrom(
                    advertPlaybackState.adPlaybackState(),
                    advertBreakResumePosition,
                    playbackResumePositionMillis
            );

            advertState = new AdvertState(
                    advertBreaks,
                    advertStateCallback
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
            List<AdvertBreak> advertBreaks = Collections.emptyList();
            advertState = new AdvertState(
                    advertBreaks,
                    advertStateCallback
            );
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

    long advertDurationBy(int advertBreakIndex, int advertIndexInAdvertBreak) {
        return advertState.advertDurationBy(advertBreakIndex, advertIndexInAdvertBreak);
    }

    @Override
    public void stop() {
        if (loadingAds != null) {
            loadingAds.cancel();
            loadingAds = null;
        }
        if (adPlaybackState != null && player != null && advertState != null) {
            advertState.stop(player.getCurrentPosition());
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
        advertState = null;
    }

    @Override
    public void handlePrepareError(int adGroupIndex, int adIndexInAdGroup, IOException exception) {
        if (advertState == null) {
            return;
        }
        advertState.handlePrepareError(adGroupIndex, adIndexInAdGroup, exception);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        if (reason == Player.TIMELINE_CHANGE_REASON_RESET || player == null || adPlaybackState == null || advertState == null) {
            // The player is being reset and this source will be released.
            return;
        }
        advertState.update(player.isPlayingAd(), player.getCurrentAdGroupIndex(), player.getCurrentAdIndexInAdGroup());
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (player == null || adPlaybackState == null || advertState == null) {
            // We need all of the above to be able to respond to advert events.
            return;
        }
        if (reason == DISCONTINUITY_REASON_SEEK) {
            advertState.handleSeek();
        }

        advertState.update(player.isPlayingAd(), player.getCurrentAdGroupIndex(), player.getCurrentAdIndexInAdGroup());
    }

    @Override
    public void onAdvertClicked() {
        if (advertState == null) {
            return;
        }
        advertState.clickAdvert();
    }

    void disableAdverts() {
        if (adPlaybackState == null || player == null || advertState == null) {
            return;
        }
        advertState.disableAdverts();
    }

    void skipAdvertBreak() {
        if (adPlaybackState == null || player == null || advertState == null) {
            return;
        }
        advertState.skipAdvertBreak();
    }

    void skipAdvert() {
        if (adPlaybackState == null || player == null || advertState == null) {
            return;
        }
        advertState.skipAdvert();
    }

    void enableAdverts() {
        if (adPlaybackState == null || player == null || advertState == null) {
            return;
        }
        advertState.enableAdverts();
    }

    private final AdvertState.Callback advertStateCallback = new AdvertState.Callback() {
        @Override
        public void onHandledPrepareError(int currentAdvertBreakIndex, int currentAdvertIndex, Advert advert, IOException exception) {
            if (adPlaybackState == null) {
                return;
            }
            adPlaybackState = adPlaybackState.withAdLoadError(currentAdvertBreakIndex, currentAdvertIndex);
            updateAdPlaybackState();
            advertListener.onAdvertPrepareError(advert, exception);
        }

        @Override
        public void onAdvertBreakStart(AdvertBreak advertBreak) {
            advertListener.onAdvertBreakStart(advertBreak);
        }

        @Override
        public void onAdvertStart(Advert advert) {
            advertListener.onAdvertStart(advert);
        }

        @Override
        public void onAdvertEnd(Advert advert) {
            advertListener.onAdvertEnd(advert);
        }

        @Override
        public void onAdvertBreakEnd(AdvertBreak advertBreak) {
            advertListener.onAdvertBreakEnd(advertBreak);
        }

        @Override
        public void onAdvertPlayed(int advertBreakIndex, int advertIndex) {
            adPlaybackState = adPlaybackState.withPlayedAd(advertBreakIndex, advertIndex);
            updateAdPlaybackState();
        }

        @Override
        public void onAdvertsDisabled(List<AdvertBreak> advertBreaks) {
            advertListener.onAdvertsDisabled();
        }

        @Override
        public void onAdvertsEnabled(List<AdvertBreak> advertBreaks) {
            AvailableAdverts.markSkippedAdvertsAsAvailable(advertBreaks, adPlaybackState);
            adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(player.getContentPosition(), advertBreaks, adPlaybackState);
            updateAdPlaybackState();
            advertListener.onAdvertsEnabled(advertBreaks);
        }

        @Override
        public void onAdvertBreakSkipped(int advertBreakIndex, AdvertBreak advertBreak) {
            adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(advertBreakIndex, adPlaybackState);
            updateAdPlaybackState();
            advertListener.onAdvertBreakSkipped(advertBreak);
        }

        @Override
        public void skipDisabledAdvertBreak(int advertBreakIndex, AdvertBreak advertBreak) {
            adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(advertBreakIndex, adPlaybackState);
            updateAdPlaybackState();
        }

        @Override
        public void reenableSkippedAdverts(List<AdvertBreak> advertBreaks) {
            AvailableAdverts.markSkippedAdvertsAsAvailable(advertBreaks, adPlaybackState);
            updateAdPlaybackState();
        }

        @Override
        public void onAdvertSkipped(int advertBreakIndex, int advertIndex, Advert advert) {
            adPlaybackState = SkippedAdverts.markAdvertAsSkipped(advertIndex, advertBreakIndex, adPlaybackState);
            updateAdPlaybackState();
            advertListener.onAdvertSkipped(advert);
        }

        @Override
        public void onAdvertClicked(Advert advert) {
            advertListener.onAdvertClicked(advert);
        }

        @Override
        public void markAdvertResumePosition(long stopPositionInMillis) {
            adPlaybackState = adPlaybackState.withAdResumePositionUs(TimeUnit.MILLISECONDS.toMicros(player.getCurrentPosition()));
        }
    };
}
