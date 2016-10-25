package com.novoda.noplayer.exoplayer;

abstract class ExoPlayerErrorListener implements ExoPlayerFacade.Listener {

    @Override
    public final void onStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as an error listener only
    }

    @Override
    public final void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // This class must be used as an error listener only
    }
}
