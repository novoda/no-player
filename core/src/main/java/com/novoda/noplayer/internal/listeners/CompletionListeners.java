package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class CompletionListeners implements NoPlayer.CompletionListener {

    private final Set<NoPlayer.CompletionListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.CompletionListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.CompletionListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onCompletion() {
        for (NoPlayer.CompletionListener listener : listeners) {
            listener.onCompletion();
        }
    }
}
