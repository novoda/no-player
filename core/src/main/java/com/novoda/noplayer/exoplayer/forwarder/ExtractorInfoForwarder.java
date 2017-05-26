package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.listeners.InfoListeners;

import java.io.IOException;
import java.util.HashMap;

class ExtractorInfoForwarder implements ExtractorMediaSource.EventListener {

    private final InfoListeners infoListeners;

    ExtractorInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onLoadError(IOException error) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("error", String.valueOf(error));

        infoListeners.onNewInfo("onLoadError", keyValuePairs);
    }
}
