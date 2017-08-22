package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

class EventInfoForwarder implements Player.EventListener {

    private final NoPlayer.InfoListener infoListener;

    EventInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("timeline", String.valueOf(timeline));
        callingMethodParameters.put("manifest", String.valueOf(manifest));

        infoListener.onNewInfo("onTimelineChanged", callingMethodParameters);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("trackGroups", String.valueOf(trackGroups));
        callingMethodParameters.put("trackSelections", String.valueOf(trackSelections));

        infoListener.onNewInfo("onTracksChanged", callingMethodParameters);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("isLoading", String.valueOf(isLoading));

        infoListener.onNewInfo("onLoadingChanged", callingMethodParameters);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("playWhenReady", String.valueOf(playWhenReady));
        callingMethodParameters.put("playbackState", String.valueOf(playbackState));

        infoListener.onNewInfo("onPlayerStateChanged", callingMethodParameters);
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("repeatMode", String.valueOf(repeatMode));

        infoListener.onNewInfo("onRepeatModeChanged", callingMethodParameters);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("error", String.valueOf(error));

        infoListener.onNewInfo("onPlayerError", callingMethodParameters);
    }

    @Override
    public void onPositionDiscontinuity() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListener.onNewInfo("onPositionDiscontinuity", callingMethodParameters);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("playbackParameters", String.valueOf(playbackParameters));

        infoListener.onNewInfo("onPlaybackParametersChanged", callingMethodParameters);
    }
}
