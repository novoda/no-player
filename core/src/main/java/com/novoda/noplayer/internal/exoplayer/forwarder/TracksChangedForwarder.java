package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;

class TracksChangedForwarder implements Player.EventListener {

    private final NoPlayer.TracksChangedListener tracksChangedListener;
    private int discontinuityReason = -1;

    TracksChangedForwarder(NoPlayer.TracksChangedListener tracksChangedListener) {
        this.tracksChangedListener = tracksChangedListener;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // no-op.
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
        // no-op.
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        // no-op.
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
        // no-op.
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        if (discontinuityReason == Player.DISCONTINUITY_REASON_SEEK && trackGroups.isEmpty()) {
            // ignore changes to empty track groups after seek
        } else {
            tracksChangedListener.onTracksChanged();
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // no-op.
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // no-op.
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        discontinuityReason = reason;
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // no-op.
    }

    @Override
    public void onSeekProcessed() {
        // no-op.
    }
}
