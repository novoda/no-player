package com.novoda.noplayer.mediaplayer.forwarder;

import com.novoda.noplayer.mediaplayer.CheckBufferHeartbeatCallback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HeartBeatListener implements CheckBufferHeartbeatCallback.BufferListener {

    private final List<CheckBufferHeartbeatCallback.BufferListener> listeners = new CopyOnWriteArrayList<>();

    public void add(CheckBufferHeartbeatCallback.BufferListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onBufferStart() {
        for (CheckBufferHeartbeatCallback.BufferListener listener : listeners) {
            listener.onBufferStart();
        }
    }

    @Override
    public void onBufferComplete() {
        for (CheckBufferHeartbeatCallback.BufferListener listener : listeners) {
            listener.onBufferComplete();
        }
    }
}
