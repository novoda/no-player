package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class EventListener implements ExoPlayer.EventListener {

    private final List<ExoPlayer.EventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(ExoPlayer.EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onTimelineChanged(timeline, manifest);
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onTracksChanged(trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onLoadingChanged(isLoading);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onPlayerError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onPositionDiscontinuity();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        for (ExoPlayer.EventListener listener : listeners) {
            listener.onPlaybackParametersChanged(playbackParameters);
        }
    }
}
