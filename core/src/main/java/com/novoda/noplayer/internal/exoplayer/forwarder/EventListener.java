package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class EventListener implements Player.EventListener {

    private final List<Player.EventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(Player.EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
        for (Player.EventListener listener : listeners) {
            listener.onTimelineChanged(timeline, manifest, reason);
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        for (Player.EventListener listener : listeners) {
            listener.onTracksChanged(trackGroups, trackSelections);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        for (Player.EventListener listener : listeners) {
            listener.onLoadingChanged(isLoading);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (Player.EventListener listener : listeners) {
            listener.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    @Override
    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
        for (Player.EventListener listener : listeners) {
            listener.onRepeatModeChanged(repeatMode);
        }
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        for (Player.EventListener listener : listeners) {
            listener.onShuffleModeEnabledChanged(shuffleModeEnabled);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (Player.EventListener listener : listeners) {
            listener.onPlayerError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        for (Player.EventListener listener : listeners) {
            listener.onPositionDiscontinuity(reason);
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        for (Player.EventListener listener : listeners) {
            listener.onPlaybackParametersChanged(playbackParameters);
        }
    }

    @Override
    public void onSeekProcessed() {
        for (Player.EventListener listener : listeners) {
            listener.onSeekProcessed();
        }
    }
}
