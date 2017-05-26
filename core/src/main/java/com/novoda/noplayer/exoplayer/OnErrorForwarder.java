package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.playererror.MediaSourceError;
import com.novoda.noplayer.listeners.ErrorListeners;

import java.io.IOException;

class OnErrorForwarder extends ExoPlayerEventForwarder {

    private final Player player;
    private final ErrorListeners errorListeners;

    OnErrorForwarder(Player player, ErrorListeners errorListeners) {
        this.player = player;
        this.errorListeners = errorListeners;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Player.PlayerError playerError = ExoPlayerErrorFactory.errorFor(error);
        errorListeners.onError(player, playerError);
    }

    @Override
    public void onLoadError(IOException error) {
        Player.PlayerError playerError = new MediaSourceError(error);
        errorListeners.onError(player, playerError);
    }

}
