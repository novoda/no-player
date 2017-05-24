package com.novoda.noplayer.exoplayer;

abstract class ExoPlayerErrorForwarder implements ExoPlayerTwoFacade.Forwarder {

    @Override
    public void forwardPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as an error forwarder only
    }

    @Override
    public void forwardVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // This class must be used as an error forwarder only
    }
}
