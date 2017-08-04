package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class StateChangedListeners implements Player.StateChangedListener {

    private enum State {
        PLAYING,
        PAUSED,
        STOPPED
    }

    private State currentState;

    private final Set<Player.StateChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(Player.StateChangedListener listener) {
        listeners.add(listener);
    }

    void remove(Player.StateChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onVideoPlaying() {
        if (currentState == State.PLAYING) {
            return;
        }

        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoPlaying();
        }

        currentState = State.PLAYING;
    }

    @Override
    public void onVideoPaused() {
        if (currentState == State.PAUSED) {
            return;
        }

        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoPaused();
        }

        currentState = State.PAUSED;
    }

    @Override
    public void onVideoStopped() {
        if (currentState == State.STOPPED) {
            return;
        }

        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoStopped();
        }

        currentState = State.STOPPED;
    }
}
