package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class BufferStateListeners implements Player.BufferStateListener {

    private final Set<Player.BufferStateListener> listeners = new CopyOnWriteArraySet<>();

    void add(Player.BufferStateListener listener) {
        listeners.add(listener);
    }

    void remove(Player.BufferStateListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onBufferStarted() {
        for (Player.BufferStateListener listener : listeners) {
            listener.onBufferStarted();
        }
    }

    @Override
    public void onBufferCompleted() {
        for (Player.BufferStateListener listener : listeners) {
            listener.onBufferCompleted();
        }
    }
}
