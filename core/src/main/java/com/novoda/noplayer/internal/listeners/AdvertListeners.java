package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertId;
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
    public void onAdvertBreakStart(AdvertBreak advertBreak) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakStart(advertBreak);
        }
    }

    @Override
    public void onAdvertBreakEnd(AdvertBreak advertBreak) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakEnd(advertBreak);
        }
    }

    @Override
    public void onAdvertStart(AdvertId advertId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertStart(advertId);
        }
    }

    @Override
    public void onAdvertEnd(AdvertId advertId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertEnd(advertId);
        }
    }

    @Override
    public void onAdvertEvent(String event) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertEvent(event);
        }
    }
}
