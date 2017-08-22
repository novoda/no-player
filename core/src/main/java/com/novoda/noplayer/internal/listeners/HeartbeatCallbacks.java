package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class HeartbeatCallbacks implements NoPlayer.HeartbeatCallback {

    private final Set<NoPlayer.HeartbeatCallback> callbacks = new CopyOnWriteArraySet<>();

    void registerCallback(NoPlayer.HeartbeatCallback heartbeatCallback) {
        callbacks.add(heartbeatCallback);
    }

    void clear() {
        callbacks.clear();
    }

    @Override
    public void onBeat(NoPlayer player) {
        for (NoPlayer.HeartbeatCallback callback : callbacks) {
            callback.onBeat(player);
        }
    }

    void unregisterCallback(NoPlayer.HeartbeatCallback heartbeatCallback) {
        callbacks.remove(heartbeatCallback);
    }
}
