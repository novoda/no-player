package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.StateChangedListeners;

class CompletionStateChangedForwarder implements MediaPlayer.OnCompletionListener {

    private final StateChangedListeners stateChangedListeners;

    CompletionStateChangedForwarder(StateChangedListeners stateChangedListeners) {
        this.stateChangedListeners = stateChangedListeners;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stateChangedListeners.onVideoStopped();
    }
}
