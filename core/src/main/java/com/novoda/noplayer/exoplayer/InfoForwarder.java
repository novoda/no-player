package com.novoda.noplayer.exoplayer;

import com.novoda.noplayer.listeners.InfoListeners;

class InfoForwarder implements InfoListener {

    private final InfoListeners infoListeners;

    InfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        infoListeners.onDroppedFrames(count, elapsed);
    }

}
