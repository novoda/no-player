package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.PreparedListeners;

class OnPreparedForwarder extends ExoPlayerStateChangedListener {

    private final PreparedListeners preparedListeners;
    private final PlayerState playerState;
    private boolean hasPrepared;

    OnPreparedForwarder(PlayerState playerState, PreparedListeners preparedListeners) {
        this.playerState = playerState;
        this.preparedListeners = preparedListeners;
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
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
