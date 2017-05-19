package com.novoda.noplayer.exoplayer;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;

abstract class ExoPlayerErrorListener implements ExoPlayerTwoFacade.Listener {

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // TODO : Implement
        Log.e("!!!", error.getMessage());
    }
}
