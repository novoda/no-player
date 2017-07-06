package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class PreparedListeners implements Player.PreparedListener {

    private final Set<Player.PreparedListener> listeners = new CopyOnWriteArraySet<>();

    private boolean hasPrepared = false;

    void add(Player.PreparedListener listener) {
        listeners.add(listener);
    }

    void remove(Player.PreparedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onPrepared(PlayerState playerState) {
        if (hasNotPreviouslyPrepared()) {
            hasPrepared = true;
            for (Player.PreparedListener listener : listeners) {
                listener.onPrepared(playerState);
            }
        }
    }

    void resetPreparedState() {
        hasPrepared = false;
    }

    private boolean hasNotPreviouslyPrepared() {
        return !hasPrepared;
    }
}
