package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class ErrorListeners implements NoPlayer.ErrorListener {

    private final Set<NoPlayer.ErrorListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.ErrorListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.ErrorListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onError(NoPlayer.PlayerError error) {
        for (NoPlayer.ErrorListener listener : listeners) {
            listener.onError(error);
        }
    }
}
