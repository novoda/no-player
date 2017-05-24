package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ErrorListeners implements Player.ErrorListener {

    private final Set<Player.ErrorListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.ErrorListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.ErrorListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onError(Player player, Player.PlayerError error) {
        for (Player.ErrorListener listener : listeners) {
            listener.onError(player, error);
        }
    }
}
