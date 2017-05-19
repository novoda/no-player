package com.novoda.noplayer.exoplayer;

abstract class ExoPlayerErrorListener implements ExoPlayerTwoFacade.Listener {

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as a error listener only
    }
}
