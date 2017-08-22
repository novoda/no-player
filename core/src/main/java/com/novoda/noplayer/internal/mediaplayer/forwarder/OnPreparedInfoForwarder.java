package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

class OnPreparedInfoForwarder implements MediaPlayer.OnPreparedListener {

    private final NoPlayer.InfoListener infoListener;

    OnPreparedInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));

        infoListener.onNewInfo("onPrepared", callingMethodParameters);
    }
}
