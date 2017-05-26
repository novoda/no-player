package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class ErrorInfoForwarder implements MediaPlayer.OnErrorListener {

    private final InfoListeners infoListeners;

    public ErrorInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("mp", String.valueOf(mp));
        keyValuePairs.put("what", String.valueOf(what));
        keyValuePairs.put("extra", String.valueOf(extra));

        infoListeners.onNewInfo("onError", keyValuePairs);
        return false;
    }
}
