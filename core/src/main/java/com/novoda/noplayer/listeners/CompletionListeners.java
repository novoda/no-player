package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CompletionListeners implements Player.CompletionListener {

    private final Set<Player.CompletionListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.CompletionListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.CompletionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onCompletion() {
        for (Player.CompletionListener listener : listeners) {
            listener.onCompletion();
        }
    }
}
