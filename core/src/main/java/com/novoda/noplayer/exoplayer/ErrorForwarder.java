package com.novoda.noplayer.exoplayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.listeners.ErrorListeners;

class ErrorForwarder extends ExoPlayerErrorListener {

    private final Player player;
    private final ErrorListeners errorListeners;

    ErrorForwarder(Player player, ErrorListeners errorListeners) {
        this.player = player;
        this.errorListeners = errorListeners;
    }

    @Override
    public void onError(Exception e) {
        Player.PlayerError playerError = ExoPlayerErrorFactory.errorFor(e);
        errorListeners.onError(player, playerError);
    }

}
