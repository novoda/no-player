package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlayer;
import com.novoda.noplayer.listeners.CompletionListeners;

class OnCompletionForwarder extends ExoPlayerEventForwarder {

    private final CompletionListeners completionListeners;

    OnCompletionForwarder(CompletionListeners completionListeners) {
        this.completionListeners = completionListeners;
    }

    @Override
    public void forwardPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            completionListeners.onCompletion();
        }
    }
}
