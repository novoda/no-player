package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class ErrorInfoForwarder implements MediaPlayer.OnErrorListener {

    private final InfoListeners infoListeners;

    ErrorInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));
        callingMethodParameters.put("what", String.valueOf(what));
        callingMethodParameters.put("extra", String.valueOf(extra));

        infoListeners.onNewInfo("onError", callingMethodParameters);
        return false;
    }
}
