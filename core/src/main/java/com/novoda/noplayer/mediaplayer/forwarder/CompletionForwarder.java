package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.CompletionListeners;

class CompletionForwarder implements MediaPlayer.OnCompletionListener {
    private final CompletionListeners completionListeners;

    public CompletionForwarder(CompletionListeners completionListeners) {
        this.completionListeners = completionListeners;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        completionListeners.onCompletion();
    }
}
