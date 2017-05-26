package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class EventInfoForwarder implements ExoPlayer.EventListener {

    private final InfoListeners infoListeners;

    EventInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("timeline", String.valueOf(timeline));
        keyValuePairs.put("manifest", String.valueOf(manifest));

        infoListeners.onNewInfo("onTimelineChanged", keyValuePairs);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("trackGroups", String.valueOf(trackGroups));
        keyValuePairs.put("trackSelections", String.valueOf(trackSelections));

        infoListeners.onNewInfo("onTracksChanged", keyValuePairs);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("isLoading", String.valueOf(isLoading));

        infoListeners.onNewInfo("onLoadingChanged", keyValuePairs);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("playWhenReady", String.valueOf(playWhenReady));
        keyValuePairs.put("playbackState", String.valueOf(playbackState));

        infoListeners.onNewInfo("onPlayerStateChanged", keyValuePairs);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("error", String.valueOf(error));

        infoListeners.onNewInfo("onPlayerError", keyValuePairs);
    }

    @Override
    public void onPositionDiscontinuity() {
        HashMap<String, String> keyValuePairs = new HashMap<>();

        infoListeners.onNewInfo("onPositionDiscontinuity", keyValuePairs);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("playbackParameters", String.valueOf(playbackParameters));

        infoListeners.onNewInfo("onPlaybackParametersChanged", keyValuePairs);
    }
}
