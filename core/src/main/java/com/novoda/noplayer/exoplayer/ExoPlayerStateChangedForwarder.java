package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

abstract class ExoPlayerStateChangedForwarder implements ExoPlayerTwoFacade.Forwarder {

    @Override
    public void forwardPlayerError(ExoPlaybackException error) {
        // This class must be used as a state changed forwarder only
    }

    @Override
    public void forwardVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // This class must be used as a state changed forwarder only
    }
}
