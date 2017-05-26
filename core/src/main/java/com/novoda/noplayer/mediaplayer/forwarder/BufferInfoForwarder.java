package com.novoda.noplayer.mediaplayer.forwarder;

import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.mediaplayer.CheckBufferHeartbeatCallback;

import java.util.HashMap;

class BufferInfoForwarder implements CheckBufferHeartbeatCallback.BufferListener {

    private final InfoListeners infoListeners;

    public BufferInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onBufferStart() {
        HashMap<String, String> keyValuePairs = new HashMap<>();

        infoListeners.onNewInfo("onBufferStart", keyValuePairs);

    }

    @Override
    public void onBufferComplete() {
        HashMap<String, String> keyValuePairs = new HashMap<>();

        infoListeners.onNewInfo("onBufferStart", keyValuePairs);
    }
}
