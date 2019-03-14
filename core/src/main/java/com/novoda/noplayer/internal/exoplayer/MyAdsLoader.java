package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.internal.utils.AdvertBreakUtils;

import java.io.IOException;
import java.util.Collections;
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
        for (int contentType : contentTypes) {
            Log.e("LOADER", "setSupportedContentTypes: " + contentType);
        }
    }

    @Override
    public void start(final EventListener eventListener, AdViewProvider adViewProvider) {
        Log.e("LOADER", "Starting load");
        this.eventListener = eventListener;
        if (adPlaybackState == null) {
            Log.e("LOADER", "calling client load");
            loader.load(advertsLoadedCallback);
        }
    }

    private final AdvertsLoader.Callback advertsLoadedCallback = new AdvertsLoader.Callback() {
        @Override
        public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
            Collections.sort(advertBreaks, new AdvertBreakStartTimeComparer());
            Log.e("LOADER", "adsLoaded transforming");
            long[] advertOffsets = AdvertBreakUtils.advertOffsets(advertBreaks);
            long[][] advertBreaksWithAdvertDurations = getAdvertBreakDurations(advertBreaks);

            adPlaybackState = new AdPlaybackState(advertOffsets);
            adPlaybackState = adPlaybackState.withAdDurationsUs(advertBreaksWithAdvertDurations);

            for (int i = 0; i < advertBreaks.size(); i++) {
                List<Advert> adverts = advertBreaks.get(i).adverts();

                adPlaybackState = adPlaybackState.withAdCount(i, adverts.size());

                for (int j = 0; j < adverts.size(); j++) {
                    Advert advert = adverts.get(j);
                    adPlaybackState = adPlaybackState.withAdUri(i, j, advert.uri());
                }
            }

            Log.e("LOADER", "retrieved adverts");

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.e("LOADER", "send to main thread");
                    updateAdPlaybackState();
                }
            });
        }

        @Override
        public void onAdvertsError(String message) {
            eventListener.onAdLoadError(null, null);
            Log.e("LOADER", "fail: " + message);
        }
    };

    private void updateAdPlaybackState() {
        if (eventListener != null) {
            Log.e("LOADER", "playback state: " + adPlaybackState);
            eventListener.onAdPlaybackState(adPlaybackState);
        }
    }

    @Override
    public void stop() {
        Log.e("LOADER", "Stopping load");
        if (adPlaybackState != null && player != null && adIndexInGroup != -1 && adGroupIndex != -1) {
            adPlaybackState = adPlaybackState.withAdResumePositionUs(TimeUnit.MILLISECONDS.toMicros(player.getCurrentPosition()));
        }
        eventListener = null;
    }

    @Override
    public void setPlayer(@Nullable Player player) {
        Log.e("LOADER", "setPlayer");
        if (player != null) {
            this.player = player;
            this.player.addListener(this);
        }
    }

    @Override
    public void release() {
        Log.e("LOADER", "release");
        adPlaybackState = null;
        player = null;
    }

    @Override
    public void handlePrepareError(int adGroupIndex, int adIndexInAdGroup, IOException exception) {
        if (adPlaybackState != null) {
            Log.e("LOADER", "group: " + adGroupIndex + " ad: " + adIndexInAdGroup + " handlePrepareError: " + exception);
            adPlaybackState = adPlaybackState.withAdLoadError(adGroupIndex, adIndexInAdGroup);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        Log.e("LOADER", "Timeline changed");
        if (reason == Player.TIMELINE_CHANGE_REASON_RESET) {
            // The player is being reset and this source will be released.
            return;
        }

        if (player != null) {
            Log.e("LOADER", "reason " + reason + " contentPosition " + player.getContentPosition() + " currentPosition " + player.getCurrentPosition() + " adIndex " + player.getCurrentAdIndexInAdGroup() + " adGroup " + player.getCurrentAdGroupIndex());
            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.e("LOADER", "Track changed");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e("LOADER", "Loading changed");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.e("LOADER", "PlayerState changed");
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.e("LOADER", "RepeatMode changed");
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.e("LOADER", "ShuffleMode changed");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e("LOADER", "PlayerError " + error.getMessage());
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.e("LOADER", "Playback params changed");
    }

    @Override
    public void onSeekProcessed() {
        Log.e("LOADER", "Seek processed");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.e("LOADER", "Position Discontinuity ");
        if (player != null) {
            Log.e("LOADER", " reason " + reason + " contentPosition " + player.getContentPosition() + " currentPosition " + player.getCurrentPosition() + " adIndex " + player.getCurrentAdIndexInAdGroup() + " adGroup " + player.getCurrentAdGroupIndex());
        }
        if (reason == Player.DISCONTINUITY_REASON_AD_INSERTION && player != null && adPlaybackState != null) {
            if (adGroupIndex != -1 && adIndexInGroup != -1) {
                adPlaybackState = adPlaybackState.withPlayedAd(adGroupIndex, adIndexInGroup);
                updateAdPlaybackState();
            }

            adGroupIndex = player.getCurrentAdGroupIndex();
            adIndexInGroup = player.getCurrentAdIndexInAdGroup();
        }
    }

    private static long[][] getAdvertBreakDurations(List<AdvertBreak> advertBreaks) {
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaks.size()][];
        for (int i = 0; i < advertBreaks.size(); i++) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();
            long[] advertDurations = new long[adverts.size()];

            for (int j = 0; j < adverts.size(); j++) {
                advertDurations[j] = adverts.get(j).durationInMicros();
                Log.e("LOADER", "AdvertDuration: " + advertDurations[j]);
            }
            advertBreaksWithAdvertDurations[i] = advertDurations;
        }
        return advertBreaksWithAdvertDurations;
    }
}
