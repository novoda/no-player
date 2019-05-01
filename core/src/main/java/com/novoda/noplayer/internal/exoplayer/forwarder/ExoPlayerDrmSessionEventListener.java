package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerDrmSessionEventListener implements DefaultDrmSessionEventListener {

    private final List<DefaultDrmSessionEventListener> listeners = new CopyOnWriteArrayList<>();

    void add(DefaultDrmSessionEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onDrmKeysLoaded() {
        for (DefaultDrmSessionEventListener listener : listeners) {
            listener.onDrmKeysLoaded();
        }
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {
        for (DefaultDrmSessionEventListener listener : listeners) {
            listener.onDrmSessionManagerError(e);
        }
    }

    @Override
    public void onDrmKeysRestored() {
        for (DefaultDrmSessionEventListener listener : listeners) {
            listener.onDrmKeysRestored();
        }
    }

    @Override
    public void onDrmKeysRemoved() {
        for (DefaultDrmSessionEventListener listener : listeners) {
            listener.onDrmKeysRemoved();
        }
    }
}
