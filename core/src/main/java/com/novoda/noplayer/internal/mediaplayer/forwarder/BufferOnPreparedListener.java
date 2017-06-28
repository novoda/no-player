package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

class BufferOnPreparedListener implements MediaPlayer.OnPreparedListener {

    private final Player.BufferStateListener bufferStateListener;

    BufferOnPreparedListener(Player.BufferStateListener bufferStateListener) {
        this.bufferStateListener = bufferStateListener;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        bufferStateListener.onBufferCompleted();
    }
}
