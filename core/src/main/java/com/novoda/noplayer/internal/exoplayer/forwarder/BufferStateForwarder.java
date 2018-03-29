package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

class BufferStateForwarder implements Player.EventListener {

    private final NoPlayer.BufferStateListener bufferStateListener;

    BufferStateForwarder(NoPlayer.BufferStateListener bufferStateListener) {
        this.bufferStateListener = bufferStateListener;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            bufferStateListener.onBufferStarted();
        } else if (playbackState == Player.STATE_READY) {
            bufferStateListener.onBufferCompleted();
        }
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
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
