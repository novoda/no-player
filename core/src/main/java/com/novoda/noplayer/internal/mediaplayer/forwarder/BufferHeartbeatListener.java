package com.novoda.noplayer.internal.mediaplayer.forwarder;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.mediaplayer.CheckBufferHeartbeatCallback;

class BufferHeartbeatListener implements CheckBufferHeartbeatCallback.BufferListener {

    private final NoPlayer.BufferStateListener bufferStateListener;

    BufferHeartbeatListener(NoPlayer.BufferStateListener bufferStateListener) {
        this.bufferStateListener = bufferStateListener;
    }

    @Override
    public void onBufferStart() {
        bufferStateListener.onBufferStarted();
    }

    @Override
    public void onBufferComplete() {
        bufferStateListener.onBufferCompleted();
    }
}
