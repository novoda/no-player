package com.novoda.noplayer.internal.mediaplayer.forwarder;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.mediaplayer.CheckBufferHeartbeatCallback;

import java.util.HashMap;

class BufferInfoForwarder implements CheckBufferHeartbeatCallback.BufferListener {

    private final NoPlayer.InfoListener infoListener;

    BufferInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onBufferStart() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListener.onNewInfo("onBufferStart", callingMethodParameters);
    }

    @Override
    public void onBufferComplete() {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        infoListener.onNewInfo("onBufferStart", callingMethodParameters);
    }
}
