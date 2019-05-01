package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

import java.util.Collections;
import java.util.HashMap;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

class EventInfoForwarder implements Player.EventListener {

    private final NoPlayer.InfoListener infoListener;

    EventInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.TIMELINE, String.valueOf(timeline));
        callingMethodParameters.put(Parameters.MANIFEST, String.valueOf(manifest));
        callingMethodParameters.put(Parameters.REASON, String.valueOf(reason));

        infoListener.onNewInfo(Methods.ON_TIMELINE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.TRACK_GROUPS, String.valueOf(trackGroups));
        callingMethodParameters.put(Parameters.TRACK_SELECTIONS, String.valueOf(trackSelections));

        infoListener.onNewInfo(Methods.ON_TRACKS_CHANGED, callingMethodParameters);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.IS_LOADING, String.valueOf(isLoading));

        infoListener.onNewInfo(Methods.ON_LOADING_CHANGED, callingMethodParameters);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.PLAY_WHEN_READY, String.valueOf(playWhenReady));
        callingMethodParameters.put(Parameters.PLAYBACK_STATE, String.valueOf(playbackState));

        infoListener.onNewInfo(Methods.ON_PLAYER_STATE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.REPEAT_MODE, String.valueOf(repeatMode));

        infoListener.onNewInfo(Methods.ON_REPEAT_MODE_CHANGED, callingMethodParameters);
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.SHUFFLE_MODE_ENABLED, String.valueOf(shuffleModeEnabled));

        infoListener.onNewInfo(Methods.ON_SHUFFLE_MODE_ENABLED_CHANGED, callingMethodParameters);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.ERROR, String.valueOf(error));

        infoListener.onNewInfo(Methods.ON_PLAYER_ERROR, callingMethodParameters);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.REASON, String.valueOf(reason));

        infoListener.onNewInfo(Methods.ON_POSITION_DISCONTINUITY, callingMethodParameters);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.PLAYBACK_PARAMETERS, String.valueOf(playbackParameters));

        infoListener.onNewInfo(Methods.ON_PLAYBACK_PARAMETERS_CHANGED, callingMethodParameters);
    }

    @Override
    public void onSeekProcessed() {
        infoListener.onNewInfo(Methods.ON_POSITION_DISCONTINUITY, Collections.<String, String>emptyMap());
    }
}
