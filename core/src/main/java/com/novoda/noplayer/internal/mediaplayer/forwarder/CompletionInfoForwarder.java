package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

import java.util.HashMap;

class CompletionInfoForwarder implements MediaPlayer.OnCompletionListener {

    private final Player.InfoListener infoListener;

    CompletionInfoForwarder(Player.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));

        infoListener.onNewInfo("onCompletion", callingMethodParameters);
    }
}
