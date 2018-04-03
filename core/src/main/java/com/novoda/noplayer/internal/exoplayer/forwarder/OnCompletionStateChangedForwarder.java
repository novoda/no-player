package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

class OnCompletionStateChangedForwarder implements Player.EventListener {

    private final NoPlayer.StateChangedListener stateChangedListener;

    OnCompletionStateChangedForwarder(NoPlayer.StateChangedListener stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            stateChangedListener.onVideoStopped();
        }
    }

    @Override
    public void onRepeatModeChanged(int i) {
        // TODO: should we send?
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        // TODO: should we send?
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
        // TODO: should we send?
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // TODO: should we send?
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // TODO: should we send?
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // Sent by ErrorForwarder.
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        // TODO: should we send?
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // TODO: should we send?
    }

    @Override
    public void onSeekProcessed() {
        // TODO: should we send?
    }
}
