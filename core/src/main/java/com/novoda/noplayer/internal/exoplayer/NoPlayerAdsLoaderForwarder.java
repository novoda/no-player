package com.novoda.noplayer.internal.exoplayer;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;

public class NoPlayerAdsLoaderForwarder implements AdsLoader {

    private final AdsLoader loader;
    private NoPlayer.AdvertListener advertListener = new NoPlayer.AdvertListener() {
        @Override
        public void onAdvertEvent(String event) {

        }
    };

    public NoPlayerAdsLoaderForwarder(AdsLoader loader) {
        this.loader = loader;
    }

    @Override
    public void setPlayer(@Nullable Player player) {
        loader.setPlayer(player);
        advertListener.onAdvertEvent("setPlayer");
    }

    @Override
    public void release() {
        loader.release();
        advertListener.onAdvertEvent("release");
    }

    @Override
    public void setSupportedContentTypes(int... contentTypes) {
        loader.setSupportedContentTypes(contentTypes);
        advertListener.onAdvertEvent("setContentTypes");
    }

    @Override
    public void start(final EventListener eventListener, AdViewProvider adViewProvider) {
        loader.start(new EventListener() {
            @Override
            public void onAdPlaybackState(AdPlaybackState adPlaybackState) {
                eventListener.onAdPlaybackState(adPlaybackState);
                advertListener.onAdvertEvent("onAdPlaybackState");
            }

            @Override
            public void onAdLoadError(AdsMediaSource.AdLoadException error, DataSpec dataSpec) {
                eventListener.onAdLoadError(error, dataSpec);
                advertListener.onAdvertEvent("onAdLoadError");
            }

            @Override
            public void onAdClicked() {
                eventListener.onAdClicked();
                advertListener.onAdvertEvent("onAdClicked");
            }

            @Override
            public void onAdTapped() {
                eventListener.onAdTapped();
                advertListener.onAdvertEvent("onAdTapped");
            }
        }, adViewProvider);
        advertListener.onAdvertEvent("setContentTypes");
    }

    @Override
    public void stop() {
        loader.stop();
        advertListener.onAdvertEvent("stop");
    }

    @Override
    public void handlePrepareError(int adGroupIndex, int adIndexInAdGroup, IOException exception) {
        loader.handlePrepareError(adGroupIndex, adIndexInAdGroup, exception);
        advertListener.onAdvertEvent("handlePrepareError");
    }

    public void bind(NoPlayer.AdvertListener advertListener) {
        this.advertListener = advertListener;
    }
}
