package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.novoda.noplayer.NoPlayer;

import java.util.Collections;
import java.util.HashMap;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

class DrmSessionInfoForwarder implements DefaultDrmSessionEventListener {

    private final NoPlayer.InfoListener infoListener;

    DrmSessionInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onDrmKeysLoaded() {
        infoListener.onNewInfo(Methods.ON_DRM_KEYS_LOADED, Collections.<String, String>emptyMap());
    }

    @Override
    public void onDrmSessionManagerError(Exception error) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put(Parameters.ERROR, String.valueOf(error));

        infoListener.onNewInfo(Methods.ON_DRM_SESSION_MANAGER_ERROR, callingMethodParameters);
    }

    @Override
    public void onDrmKeysRestored() {
        infoListener.onNewInfo(Methods.ON_DRM_KEYS_RESTORED, Collections.<String, String>emptyMap());

    }

    @Override
    public void onDrmKeysRemoved() {
        infoListener.onNewInfo(Methods.ON_DRM_KEYS_REMOVED, Collections.<String, String>emptyMap());
    }
}
