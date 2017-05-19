package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlayer;
import com.novoda.noplayer.listeners.CompletionListeners;

class OnCompletionForwarder extends ExoPlayerErrorListener {

    private final CompletionListeners completionListeners;

    OnCompletionForwarder(CompletionListeners completionListeners) {
        this.completionListeners = completionListeners;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            completionListeners.onCompletion();
        }
    }
}
