package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

class CompletionForwarder implements MediaPlayer.OnCompletionListener {

    private final Player.CompletionListener completionListener;

    CompletionForwarder(Player.CompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        completionListener.onCompletion();
    }
}
