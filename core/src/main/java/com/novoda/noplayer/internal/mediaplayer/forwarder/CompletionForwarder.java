package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

class CompletionForwarder implements MediaPlayer.OnCompletionListener {

    private final NoPlayer.CompletionListener completionListener;

    CompletionForwarder(NoPlayer.CompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        completionListener.onCompletion();
    }
}
