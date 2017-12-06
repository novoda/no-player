package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.PlayerState;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class PreparedListeners implements NoPlayer.PreparedListener {

    private final Set<NoPlayer.PreparedListener> listeners = new CopyOnWriteArraySet<>();

    private boolean hasPrepared;

    void add(NoPlayer.PreparedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.PreparedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onPrepared(PlayerState playerState) {
        if (!hasPrepared) {
            hasPrepared = true;
            for (NoPlayer.PreparedListener listener : listeners) {
                listener.onPrepared(playerState);
            }
        }
    }

    void resetPreparedState() {
        hasPrepared = false;
    }
}
