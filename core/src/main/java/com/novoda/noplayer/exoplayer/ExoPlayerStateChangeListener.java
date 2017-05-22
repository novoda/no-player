package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

abstract class ExoPlayerStateChangeListener implements ExoPlayerTwoFacade.Listener {

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // This class must be used as a state changed listener only
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        // This class must be used as a state changed listener only
    }
}
