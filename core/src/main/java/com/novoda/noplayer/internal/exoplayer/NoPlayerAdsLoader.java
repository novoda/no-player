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
import com.novoda.noplayer.AdvertId;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NoPlayerAdsLoader implements AdsLoader, Player.EventListener {

    private static final String TAG = "ADS";
    private final AdvertsLoader loader;
    private final Handler handler;
    private final List<AdvertBreak> advertBreaks;

    @Nullable
    private Player player;
    @Nullable
    private AdPlaybackState adPlaybackState;
    @Nullable
    private EventListener eventListener;
    @Nullable
    private AdvertsLoader.Cancellable loadingAds;

    private NoPlayer.AdvertListener advertListener = NoOpAdvertListener.INSTANCE;
    private int adIndexInGroup = -1;
    private int adGroupIndex = -1;

    NoPlayerAdsLoader(AdvertsLoader loader) {
        this.loader = loader;
        this.handler = new Handler(Looper.getMainLooper());
        this.advertBreaks = new ArrayList<>();
    }

    public void bind(Optional<NoPlayer.AdvertListener> advertListener) {
        if (advertListener.isPresent()) {
            this.advertListener = advertListener.get();
        } else {
            this.advertListener = NoOpAdvertListener.INSTANCE;
        }
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
            advertListener.onAdvertEvent("start loading adverts");
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
            advertBreaks.clear();
            advertBreaks.addAll(advertPlaybackState.advertBreaks());

            adPlaybackState = advertPlaybackState.adPlaybackState();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    advertListener.onAdvertEvent("adverts loaded");
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

        if (player != null) {
            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();

            if (reason == Player.TIMELINE_CHANGE_REASON_PREPARED && adGroupIndex != -1 && adIndexInGroup != -1) {
                Advert advert = advertBreaks.get(adGroupIndex).adverts().get(adIndexInGroup);
                advertListener.onAdvertStart(advert.advertId());
            }
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (reason == Player.DISCONTINUITY_REASON_AD_INSERTION && player != null && adPlaybackState != null) {
            if (adGroupIndex != -1 && adIndexInGroup != -1) {
                Advert advert = advertBreaks.get(adGroupIndex).adverts().get(adIndexInGroup);
                advertListener.onAdvertEnd(advert.advertId());

                adPlaybackState = adPlaybackState.withPlayedAd(adGroupIndex, adIndexInGroup);
                updateAdPlaybackState();
            }

            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();
            if (adGroupIndex != -1 && adIndexInGroup != -1) {
                Advert advert = advertBreaks.get(adGroupIndex).adverts().get(adIndexInGroup);
                advertListener.onAdvertStart(advert.advertId());
            }
        }
    }

    private enum NoOpAdvertListener implements NoPlayer.AdvertListener {
        INSTANCE;

        @Override
        public void onAdvertBreakStart(AdvertBreak advertBreak) {
            // no-op
        }

        @Override
        public void onAdvertBreakEnd(AdvertBreak advertBreak) {
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
        public void onAdvertEvent(String event) {
            // no-op
        }
    }
}
