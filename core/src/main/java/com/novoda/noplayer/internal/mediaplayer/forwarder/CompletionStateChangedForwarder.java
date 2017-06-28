package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

class CompletionStateChangedForwarder implements MediaPlayer.OnCompletionListener {

    private final Player.StateChangedListener stateChangedListener;

    CompletionStateChangedForwarder(Player.StateChangedListener stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stateChangedListener.onVideoStopped();
    }
}
