package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

class BufferOnPreparedListener implements MediaPlayer.OnPreparedListener {

    private final NoPlayer.BufferStateListener bufferStateListener;

    BufferOnPreparedListener(NoPlayer.BufferStateListener bufferStateListener) {
        this.bufferStateListener = bufferStateListener;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        bufferStateListener.onBufferCompleted();
    }
}
