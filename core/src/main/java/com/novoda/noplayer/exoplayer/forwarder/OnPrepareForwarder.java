package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.PreparedListeners;

class OnPrepareForwarder implements ExoPlayer.EventListener {

    private final PreparedListeners preparedListeners;
    private final PlayerState playerState;

    OnPrepareForwarder(PreparedListeners preparedListeners, PlayerState playerState) {
        this.preparedListeners = preparedListeners;
        this.playerState = playerState;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (isReady(playbackState)) {
            preparedListeners.onPrepared(playerState);
        }
    }

    private boolean isReady(int playbackState) {
        return playbackState == ExoPlayer.STATE_READY;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        //TODO should we send ?
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        //TODO should we send ?
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        //TODO should we send ?
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        //Sent by ErrorForwarder
    }

    @Override
    public void onPositionDiscontinuity() {
        //TODO should we send ?
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        //TODO should we send ?
    }
}
