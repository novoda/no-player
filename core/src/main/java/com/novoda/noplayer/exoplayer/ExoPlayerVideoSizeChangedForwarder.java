package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

abstract class ExoPlayerVideoSizeChangedForwarder implements ExoPlayerTwoFacade.Forwarder {

    @Override
    public void forwardPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as a video size changed forwarder only
    }

    @Override
    public void forwardPlayerError(ExoPlaybackException error) {
        // This class must be used as a video size changed forwarder only
    }
}
