package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class AdvertListeners implements NoPlayer.AdvertListener {

    private final Set<NoPlayer.AdvertListener> listeners = new CopyOnWriteArraySet<>();

    public void add(NoPlayer.AdvertListener listener) {
        listeners.add(listener);
    }

    public void remove(NoPlayer.AdvertListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onAdvertEvent(String event) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertEvent(event);
        }
    }
}
