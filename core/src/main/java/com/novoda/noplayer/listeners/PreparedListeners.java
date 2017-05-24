package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PreparedListeners implements Player.PreparedListener {

    private final Set<Player.PreparedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.PreparedListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.PreparedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onPrepared(PlayerState playerState) {
        for (Player.PreparedListener listener : listeners) {
            listener.onPrepared(playerState);
        }
    }
}
