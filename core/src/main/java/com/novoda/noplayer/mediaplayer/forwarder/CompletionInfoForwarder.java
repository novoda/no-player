package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class CompletionInfoForwarder implements MediaPlayer.OnCompletionListener {

    private final InfoListeners infoListeners;

    public CompletionInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("mp", String.valueOf(mp));

        infoListeners.onNewInfo("onCompletion", keyValuePairs);
    }
}
