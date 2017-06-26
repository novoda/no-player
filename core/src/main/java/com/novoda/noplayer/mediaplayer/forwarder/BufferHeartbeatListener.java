package com.novoda.noplayer.mediaplayer.forwarder;

import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.mediaplayer.CheckBufferHeartbeatCallback;

class BufferHeartbeatListener implements CheckBufferHeartbeatCallback.BufferListener {

    private final BufferStateListeners bufferStateListeners;

    BufferHeartbeatListener(BufferStateListeners bufferStateListeners) {
        this.bufferStateListeners = bufferStateListeners;
    }

    @Override
    public void onBufferStart() {
        bufferStateListeners.onBufferStarted();
    }

    @Override
    public void onBufferComplete() {
        bufferStateListeners.onBufferCompleted();
    }
}
