package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.internal.mediaplayer.ErrorFactory;

class ErrorForwarder implements MediaPlayer.OnErrorListener {

    private final Player.BufferStateListener bufferStateListener;
    private final Player.ErrorListener errorListener;
    private final Player player;

    ErrorForwarder(Player.BufferStateListener bufferStateListener, Player.ErrorListener errorListener, Player player) {
        this.bufferStateListener = bufferStateListener;
        this.errorListener = errorListener;
        this.player = player;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        bufferStateListener.onBufferCompleted();
        Player.PlayerError error = ErrorFactory.createErrorFrom(what, extra);
        errorListener.onError(player, error);
        return true;
    }
}
