package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.mediaplayer.ErrorFactory;

class ErrorForwarder implements MediaPlayer.OnErrorListener {

    private final NoPlayer.BufferStateListener bufferStateListener;
    private final NoPlayer.ErrorListener errorListener;

    ErrorForwarder(NoPlayer.BufferStateListener bufferStateListener, NoPlayer.ErrorListener errorListener) {
        this.bufferStateListener = bufferStateListener;
        this.errorListener = errorListener;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        bufferStateListener.onBufferCompleted();
        NoPlayer.PlayerError error = ErrorFactory.createErrorFrom(what, extra);
        errorListener.onError(error);
        return true;
    }
}
