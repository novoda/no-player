package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;

abstract class ExoPlayerVideoSizeChangedListener implements ExoPlayerTwoFacade.Listener {

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // This class must be used as a vido size changed listener only
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // This class must be used as a vido size changed listener only
    }

}
