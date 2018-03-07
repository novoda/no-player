package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.novoda.noplayer.NoPlayer;

import java.util.Collections;
import java.util.HashMap;

class DrmSessionInfoForwarder implements DefaultDrmSessionManager.EventListener {

    private final NoPlayer.InfoListener infoListener;

    DrmSessionInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onDrmKeysLoaded() {
        infoListener.onNewInfo("onDrmKeysLoaded", Collections.<String, String>emptyMap());

    }

    @Override
    public void onDrmSessionManagerError(Exception error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("error", String.valueOf(error));

        infoListener.onNewInfo("onDrmSessionManagerError", callingMethodParameters);
    }

    @Override
    public void onDrmKeysRestored() {
        infoListener.onNewInfo("onDrmKeysRestored", Collections.<String, String>emptyMap());

    }

    @Override
    public void onDrmKeysRemoved() {
        infoListener.onNewInfo("onDrmKeysRemoved", Collections.<String, String>emptyMap());
    }
}
