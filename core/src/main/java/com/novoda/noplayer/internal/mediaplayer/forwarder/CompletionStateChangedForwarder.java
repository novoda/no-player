package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;

class CompletionStateChangedForwarder implements MediaPlayer.OnCompletionListener {

    private final NoPlayer.StateChangedListener stateChangedListener;

    CompletionStateChangedForwarder(NoPlayer.StateChangedListener stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stateChangedListener.onVideoStopped();
    }
}
