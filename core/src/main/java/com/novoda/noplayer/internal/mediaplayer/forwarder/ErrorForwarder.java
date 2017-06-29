package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.internal.mediaplayer.ErrorFactory;

class ErrorForwarder implements MediaPlayer.OnErrorListener {

    private final Player.BufferStateListener bufferStateListener;
    private final Player.ErrorListener errorListener;

    ErrorForwarder(Player.BufferStateListener bufferStateListener, Player.ErrorListener errorListener) {
        this.bufferStateListener = bufferStateListener;
        this.errorListener = errorListener;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        bufferStateListener.onBufferCompleted();
        Player.PlayerError error = ErrorFactory.createErrorFrom(what, extra);
        errorListener.onError(error);
        return true;
    }
}
