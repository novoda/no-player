package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlayer;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.PreparedListeners;

class OnPrepareForwarder extends ExoPlayerEventForwarder {

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
}
