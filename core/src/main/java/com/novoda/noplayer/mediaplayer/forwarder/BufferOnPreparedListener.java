package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.BufferStateListeners;

class BufferOnPreparedListener implements MediaPlayer.OnPreparedListener {

    private final BufferStateListeners bufferStateListeners;

    BufferOnPreparedListener(BufferStateListeners bufferStateListeners) {
        this.bufferStateListeners = bufferStateListeners;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        bufferStateListeners.onBufferCompleted();
    }
}
