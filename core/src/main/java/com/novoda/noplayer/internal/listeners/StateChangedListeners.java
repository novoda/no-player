package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.NoPlayerLog;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class StateChangedListeners implements NoPlayer.StateChangedListener {

    private enum State {
        PLAYING,
        PAUSED,
        STOPPED
    }

    private State currentState;

    private final Set<NoPlayer.StateChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.StateChangedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.StateChangedListener listener) {
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

        for (NoPlayer.StateChangedListener listener : listeners) {
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

        for (NoPlayer.StateChangedListener listener : listeners) {
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

        for (NoPlayer.StateChangedListener listener : listeners) {
            listener.onVideoStopped();
        }

        currentState = State.STOPPED;
    }
}
