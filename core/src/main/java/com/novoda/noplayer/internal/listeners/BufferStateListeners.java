package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class BufferStateListeners implements NoPlayer.BufferStateListener {

    private final Set<NoPlayer.BufferStateListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.BufferStateListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.BufferStateListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onBufferStarted() {
        for (NoPlayer.BufferStateListener listener : listeners) {
            listener.onBufferStarted();
        }
    }

    @Override
    public void onBufferCompleted() {
        for (NoPlayer.BufferStateListener listener : listeners) {
            listener.onBufferCompleted();
        }
    }
}
