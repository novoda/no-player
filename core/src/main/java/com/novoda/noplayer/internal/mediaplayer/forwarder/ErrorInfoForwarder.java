package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

class ErrorInfoForwarder implements MediaPlayer.OnErrorListener {

    private final NoPlayer.InfoListener infoListener;

    ErrorInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));
        callingMethodParameters.put("what", String.valueOf(what));
        callingMethodParameters.put("extra", String.valueOf(extra));

        infoListener.onNewInfo("onError", callingMethodParameters);
        return false;
    }
}
