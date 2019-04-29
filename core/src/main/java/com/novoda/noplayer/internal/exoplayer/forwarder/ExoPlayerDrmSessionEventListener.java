package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevel;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevelEventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerDrmSessionEventListener implements DefaultDrmSessionEventListener, DrmSecurityLevelEventListener {

    private final List<DefaultDrmSessionEventListener> drmSessionEventListeners = new CopyOnWriteArrayList<>();
    private final List<DrmSecurityLevelEventListener> securityLevelEventListeners = new CopyOnWriteArrayList<>();

    void add(DefaultDrmSessionEventListener listener) {
        drmSessionEventListeners.add(listener);
    }

    void add(DrmSecurityLevelEventListener listener) {
        securityLevelEventListeners.add(listener);
    }

    @Override
    public void onDrmKeysLoaded() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysLoaded();
        }
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmSessionManagerError(e);
        }
    }

    @Override
    public void onDrmKeysRestored() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysRestored();
        }
    }

    @Override
    public void onDrmKeysRemoved() {
        for (DefaultDrmSessionEventListener listener : drmSessionEventListeners) {
            listener.onDrmKeysRemoved();
        }
    }

    @Override
    public void contentSecurityLevel(DrmSecurityLevel securityLevel) {
        for (DrmSecurityLevelEventListener listener : securityLevelEventListeners) {
            listener.contentSecurityLevel(securityLevel);
        }
    }
}
