package com.novoda.noplayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class HeartbeatCallbacks<T> implements Heart.Heartbeat.Callback<T> {

    private final Set<Heart.Heartbeat.Callback<T>> callbacks = new CopyOnWriteArraySet<>();

    public void registerCallback(Heart.Heartbeat.Callback<T> callback) {
        callbacks.add(callback);
    }

    @Override
    public void onBeat(T object) {
        for (Heart.Heartbeat.Callback<T> callback : callbacks) {
            callback.onBeat(object);
        }
    }

    public void unregisterCallback(Heart.Heartbeat.Callback callback) {
        callbacks.remove(callback);
    }
}
