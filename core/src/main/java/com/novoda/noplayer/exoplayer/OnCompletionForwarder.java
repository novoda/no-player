package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlayer;
import com.novoda.noplayer.listeners.CompletionListeners;

class OnCompletionForwarder extends ExoPlayerStateChangedListener {

    private final CompletionListeners completionListeners;

    OnCompletionForwarder(CompletionListeners completionListeners) {
        this.completionListeners = completionListeners;
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            completionListeners.onCompletion();
        }
    }

}
