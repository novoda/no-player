package com.novoda.noplayer.mediaplayer.forwarder;

import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.mediaplayer.CheckBufferHeartbeatCallback;

import java.util.HashMap;

class BufferInfoForwarder implements CheckBufferHeartbeatCallback.BufferListener {

    private final InfoListeners infoListeners;

    BufferInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onBufferStart() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListeners.onNewInfo("onBufferStart", callingMethodParameters);
    }

    @Override
    public void onBufferComplete() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListeners.onNewInfo("onBufferStart", callingMethodParameters);
    }
}
