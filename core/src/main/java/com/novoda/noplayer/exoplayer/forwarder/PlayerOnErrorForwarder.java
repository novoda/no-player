package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.listeners.ErrorListeners;

class PlayerOnErrorForwarder implements ExoPlayer.EventListener {

    private final Player player;
    private final ErrorListeners errorListeners;

    PlayerOnErrorForwarder(Player player, ErrorListeners errorListeners) {
        this.player = player;
        this.errorListeners = errorListeners;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Player.PlayerError playerError = ExoPlayerErrorFactory.errorFor(error);
        errorListeners.onError(player, playerError);
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
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        //Handled by OnPrepared and OnCompletion forwarders
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
