package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class CompletionInfoForwarder implements MediaPlayer.OnCompletionListener {

    private final InfoListeners infoListeners;

    CompletionInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));

        infoListeners.onNewInfo("onCompletion", callingMethodParameters);
    }
}
