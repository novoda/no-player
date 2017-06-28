package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class CompletionListeners implements Player.CompletionListener {

    private final Set<Player.CompletionListener> listeners = new CopyOnWriteArraySet<>();

    void add(Player.CompletionListener listener) {
        listeners.add(listener);
    }

    void remove(Player.CompletionListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onCompletion() {
        for (Player.CompletionListener listener : listeners) {
            listener.onCompletion();
        }
    }
}
