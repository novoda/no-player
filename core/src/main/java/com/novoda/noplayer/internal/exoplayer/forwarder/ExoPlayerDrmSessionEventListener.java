package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerDrmSessionEventListener implements DefaultDrmSessionManager.EventListener {

    private final List<DefaultDrmSessionManager.EventListener> listeners = new CopyOnWriteArrayList<>();

    void add(DefaultDrmSessionManager.EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onDrmKeysLoaded() {
        for (DefaultDrmSessionManager.EventListener listener : listeners) {
            listener.onDrmKeysLoaded();
        }
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        for (DefaultDrmSessionManager.EventListener listener : listeners) {
            listener.onDrmSessionManagerError(e);
        }
    }

    @Override
    public void onDrmKeysRestored() {
        for (DefaultDrmSessionManager.EventListener listener : listeners) {
            listener.onDrmKeysRestored();
        }
    }

    @Override
    public void onDrmKeysRemoved() {
        for (DefaultDrmSessionManager.EventListener listener : listeners) {
            listener.onDrmKeysRemoved();
        }
    }
}
