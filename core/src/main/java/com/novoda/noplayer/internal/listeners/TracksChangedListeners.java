package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class TracksChangedListeners implements NoPlayer.TracksChangedListener {

    private final Set<NoPlayer.TracksChangedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(NoPlayer.TracksChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(NoPlayer.TracksChangedListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onTracksChanged() {
        for (NoPlayer.TracksChangedListener listener : listeners) {
            listener.onTracksChanged();
        }
    }
}
