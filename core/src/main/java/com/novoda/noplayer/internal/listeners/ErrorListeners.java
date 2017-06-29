package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class ErrorListeners implements Player.ErrorListener {

    private final Set<Player.ErrorListener> listeners = new CopyOnWriteArraySet<>();

    void add(Player.ErrorListener listener) {
        listeners.add(listener);
    }

    void remove(Player.ErrorListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onError(Player player, Player.PlayerError error) {
        for (Player.ErrorListener listener : listeners) {
            listener.onError(player, error);
        }
    }
}
