package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerErrorType;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatter.formatMessage;

class DrmSessionErrorForwarder implements DefaultDrmSessionManager.EventListener {

    private final Player.ErrorListener errorListener;

    DrmSessionErrorForwarder(Player.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public void onDrmKeysLoaded() {
        // TODO: should we send?
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        Player.PlayerError playerError = new NoPlayerError(PlayerErrorType.FAILED_DRM_INITIATING, formatMessage(e));
        errorListener.onError(playerError);
    }

    @Override
    public void onDrmKeysRestored() {
        // TODO: should we send?
    }

    @Override
    public void onDrmKeysRemoved() {
        // TODO: should we send?
    }
}
