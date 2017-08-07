package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Player;
import com.novoda.utils.NoPlayerLog;

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
            NoPlayerLog.e("Tried calling onVideoPlaying() but video is already playing.");
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
            NoPlayerLog.e("Tried calling onVideoPaused() but video is already paused.");
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
            NoPlayerLog.e("Tried calling onVideoStopped() but video has already stopped.");
            return;
        }

        for (Player.StateChangedListener listener : listeners) {
            listener.onVideoStopped();
        }

        currentState = State.STOPPED;
    }
}
