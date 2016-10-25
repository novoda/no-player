package com.novoda.noplayer.exoplayer;

abstract class ExoPlayerStateChangedListener implements ExoPlayerFacade.Listener {

    @Override
    public final void onError(Exception e) {
        // This class must be used as a state changed listener only
    }

    @Override
    public final void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // This class must be used as a state changed listener only
    }
}
