package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class HeartbeatCallbacks implements Player.HeartbeatCallback {

    private final Set<Player.HeartbeatCallback> callbacks = new CopyOnWriteArraySet<>();

    void registerCallback(Player.HeartbeatCallback heartbeatCallback) {
        callbacks.add(heartbeatCallback);
    }

    void clear() {
        callbacks.clear();
    }

    @Override
    public void onBeat(Player player) {
        for (Player.HeartbeatCallback callback : callbacks) {
            callback.onBeat(player);
        }
    }

    void unregisterCallback(Player.HeartbeatCallback heartbeatCallback) {
        callbacks.remove(heartbeatCallback);
    }
}
