package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class DrmSecurityLevelCallbacks implements NoPlayer.DrmSecurityLevelCallback {

    private final Set<NoPlayer.DrmSecurityLevelCallback> callbacks = new CopyOnWriteArraySet<>();

    void add(NoPlayer.DrmSecurityLevelCallback listener) {
        callbacks.add(listener);
    }

    void remove(NoPlayer.DrmSecurityLevelCallback listener) {
        callbacks.remove(listener);
    }

    void clear() {
        callbacks.clear();
    }

    @Override
    public void onSecurityLevelDetermined(String securityLevel) {
        for (NoPlayer.DrmSecurityLevelCallback callback : callbacks) {
            callback.onSecurityLevelDetermined(securityLevel);
        }
    }
}
