package com.novoda.noplayer.exoplayer;

abstract class ExoPlayerVideoSizeChangedListener implements ExoPlayerFacade.Listener {

    @Override
    public final void onStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as a state changed listener only
    }

    @Override
    public final void onError(Exception e) {
        // This class must be used as a state changed listener only
    }
}
