package com.novoda.noplayer.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.playererror.DrmInitiatingError;
import com.novoda.noplayer.listeners.ErrorListeners;

class DrmSessionErrorForwarder implements DefaultDrmSessionManager.EventListener {

    private final Player player;
    private final ErrorListeners errorListeners;

    DrmSessionErrorForwarder(Player player, ErrorListeners errorListeners) {
        this.player = player;
        this.errorListeners = errorListeners;
    }

    @Override
    public void onDrmKeysLoaded() {
        // TODO: Are we interested?
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        Player.PlayerError playerError = new DrmInitiatingError(e);
        errorListeners.onError(player, playerError);
    }

    @Override
    public void onDrmKeysRestored() {
        // TODO: Are we interested?
    }

    @Override
    public void onDrmKeysRemoved() {
        // TODO: Are we interested?
    }
}
