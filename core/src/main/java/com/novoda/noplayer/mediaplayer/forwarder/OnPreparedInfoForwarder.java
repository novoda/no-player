package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class OnPreparedInfoForwarder implements MediaPlayer.OnPreparedListener {

    private final InfoListeners infoListeners;

    OnPreparedInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));

        infoListeners.onNewInfo("onPrepared", callingMethodParameters);
    }
}
