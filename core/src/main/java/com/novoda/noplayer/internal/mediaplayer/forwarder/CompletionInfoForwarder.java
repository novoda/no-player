package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

import java.util.HashMap;

class CompletionInfoForwarder implements MediaPlayer.OnCompletionListener {

    private final NoPlayer.InfoListener infoListener;

    CompletionInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));

        infoListener.onNewInfo("onCompletion", callingMethodParameters);
    }
}
