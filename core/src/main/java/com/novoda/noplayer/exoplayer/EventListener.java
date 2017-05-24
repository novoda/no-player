package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.List;

class EventListener implements ExoPlayer.EventListener {

    private final List<ExoPlayerTwoFacade.Forwarder> forwarders;

    EventListener(List<ExoPlayerTwoFacade.Forwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // no-op
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // no-op
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // no-op
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (ExoPlayerTwoFacade.Forwarder forwarder : forwarders) {
            forwarder.forwardPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (ExoPlayerTwoFacade.Forwarder forwarder : forwarders) {
            forwarder.forwardPlayerError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        //no-op
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // no-op
    }
}
