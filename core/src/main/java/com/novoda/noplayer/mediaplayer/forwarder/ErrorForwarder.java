package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.mediaplayer.ErrorFactory;

class ErrorForwarder implements MediaPlayer.OnErrorListener {

    private final BufferStateListeners bufferStateListeners;
    private final ErrorListeners errorListeners;
    private final Player player;

    ErrorForwarder(BufferStateListeners bufferStateListeners, ErrorListeners errorListeners, Player player) {
        this.bufferStateListeners = bufferStateListeners;
        this.errorListeners = errorListeners;
        this.player = player;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        bufferStateListeners.onBufferCompleted();
        Player.PlayerError error = ErrorFactory.createErrorFrom(what, extra);
        errorListeners.onError(player, error);
        return true;
    }
}
