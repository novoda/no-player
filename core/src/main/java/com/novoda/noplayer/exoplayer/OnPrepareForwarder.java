package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.PreparedListeners;

class OnPrepareForwarder extends ExoPlayerEventForwarder {

    private final PreparedListeners preparedListeners;
    private final PlayerState playerState;
    private boolean hasPrepared;

    OnPrepareForwarder(PreparedListeners preparedListeners, PlayerState playerState) {
        this.preparedListeners = preparedListeners;
        this.playerState = playerState;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (isReady(playbackState) && hasNotPreviouslyPrepared()) {
            hasPrepared = true;
            preparedListeners.onPrepared(playerState);
        }
    }

    private boolean isReady(int playbackState) {
        return playbackState == ExoPlayer.STATE_READY;
    }

    private boolean hasNotPreviouslyPrepared() {
        return !hasPrepared;
    }

    public void reset() {
        hasPrepared = false;
    }
}
