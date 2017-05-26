package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.List;

class EventListener implements ExoPlayer.EventListener {

    private final List<ExoPlayerForwarder> forwarders;

    EventListener(List<ExoPlayerForwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onTimelineChanged(timeline, manifest);
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onTracksChanged(trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onLoadingChanged(isLoading);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onPlayerError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onPositionDiscontinuity();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onPlaybackParametersChanged(playbackParameters);
        }
    }
}
