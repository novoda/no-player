package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StateChangedListeners implements Player.StateChangedListener {

    private final Set<Player.StateChangedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.StateChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.StateChangedListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onVideoPlaying() {
        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoPlaying();
        }
    }

    @Override
    public void onVideoPaused() {
        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoPaused();
        }
    }

    @Override
    public void onVideoStopped() {
        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoStopped();
        }
    }
}
