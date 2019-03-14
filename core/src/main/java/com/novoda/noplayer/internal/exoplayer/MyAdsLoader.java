package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertsLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAdsLoader implements AdsLoader, Player.EventListener {

    private final AdvertsLoader loader;
    @Nullable
    private Player player;
    @Nullable
    private AdPlaybackState adPlaybackState;
    @Nullable
    private EventListener eventListener;

    private int adIndexInGroup = -1;
    private int adGroupIndex = -1;

    MyAdsLoader(AdvertsLoader loader) {
        this.loader = loader;
    }

    @Override
    public void setSupportedContentTypes(int... contentTypes) {
        // no-op
    }

    @Override
    public void start(final EventListener eventListener, AdViewProvider adViewProvider) {
        this.eventListener = eventListener;
        if (adPlaybackState == null) {
            loader.load(advertsLoadedCallback);
        }
    }

    private final AdvertsLoader.Callback advertsLoadedCallback = new AdvertsLoader.Callback() {
        @Override
        public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
            adPlaybackState = AdvertPlaybackState.from(advertBreaks);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateAdPlaybackState();
                }
            });
        }

        @Override
        public void onAdvertsError(String message) {
            eventListener.onAdLoadError(null, null);
        }
    };

    private void updateAdPlaybackState() {
        if (eventListener != null) {
            eventListener.onAdPlaybackState(adPlaybackState);
        }
    }

    @Override
    public void stop() {
        if (adPlaybackState != null && player != null && player.isPlayingAd()) {
            adPlaybackState = adPlaybackState.withAdResumePositionUs(TimeUnit.MILLISECONDS.toMicros(player.getCurrentPosition()));
        }
        eventListener = null;
    }

    @Override
    public void setPlayer(@Nullable Player player) {
        if (player != null) {
            this.player = player;
            this.player.addListener(this);
        }
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
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (reason == Player.DISCONTINUITY_REASON_AD_INSERTION && player != null && adPlaybackState != null) {
            if (adGroupIndex != -1 && adIndexInGroup != -1) {
                adPlaybackState = adPlaybackState.withPlayedAd(adGroupIndex, adIndexInGroup);
                updateAdPlaybackState();
            }

            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();
        }
    }
}
