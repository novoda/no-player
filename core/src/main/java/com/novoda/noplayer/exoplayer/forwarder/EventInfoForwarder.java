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
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("timeline", String.valueOf(timeline));
        callingMethodParameters.put("manifest", String.valueOf(manifest));

        infoListeners.onNewInfo("onTimelineChanged", callingMethodParameters);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("trackGroups", String.valueOf(trackGroups));
        callingMethodParameters.put("trackSelections", String.valueOf(trackSelections));

        infoListeners.onNewInfo("onTracksChanged", callingMethodParameters);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("isLoading", String.valueOf(isLoading));

        infoListeners.onNewInfo("onLoadingChanged", callingMethodParameters);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("playWhenReady", String.valueOf(playWhenReady));
        callingMethodParameters.put("playbackState", String.valueOf(playbackState));

        infoListeners.onNewInfo("onPlayerStateChanged", callingMethodParameters);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("error", String.valueOf(error));

        infoListeners.onNewInfo("onPlayerError", callingMethodParameters);
    }

    @Override
    public void onPositionDiscontinuity() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListeners.onNewInfo("onPositionDiscontinuity", callingMethodParameters);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("playbackParameters", String.valueOf(playbackParameters));

        infoListeners.onNewInfo("onPlaybackParametersChanged", callingMethodParameters);
    }
}
