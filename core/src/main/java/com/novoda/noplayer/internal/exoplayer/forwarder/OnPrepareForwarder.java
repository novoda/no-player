package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;

class OnPrepareForwarder implements ExoPlayer.EventListener {

    private final Player.PreparedListener preparedListener;
    private final PlayerState playerState;

    OnPrepareForwarder(Player.PreparedListener preparedListener, PlayerState playerState) {
        this.preparedListener = preparedListener;
        this.playerState = playerState;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (isReady(playbackState)) {
            preparedListener.onPrepared(playerState);
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
