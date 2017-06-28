package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Heart;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class HeartbeatCallbacks<T> implements Heart.Heartbeat.Callback<T> {

    private final Set<Heart.Heartbeat.Callback<T>> callbacks = new CopyOnWriteArraySet<>();

    void registerCallback(Heart.Heartbeat.Callback<T> callback) {
        callbacks.add(callback);
    }

    void clear() {
        callbacks.clear();
    }

    @Override
    public void onBeat(T object) {
        for (Heart.Heartbeat.Callback<T> callback : callbacks) {
            callback.onBeat(object);
        }
    }

    void unregisterCallback(Heart.Heartbeat.Callback callback) {
        callbacks.remove(callback);
    }
}
