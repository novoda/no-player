package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.listeners.PreparedListeners;

class OnPreparedForwarder implements MediaPlayer.OnPreparedListener {

    private final PreparedListeners preparedListeners;
    private final PlayerState playerState;

    OnPreparedForwarder(PreparedListeners preparedListeners, PlayerState playerState) {
        this.preparedListeners = preparedListeners;
        this.playerState = playerState;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        preparedListeners.onPrepared(playerState);
    }
}
