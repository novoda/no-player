package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class FramesPerSecondChangedListeners implements NoPlayer.FramesPerSecondChangedListener {

    private final Set<NoPlayer.FramesPerSecondChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.FramesPerSecondChangedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.FramesPerSecondChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onFramesPerSecondChanged(int framesPerSecond) {
        for (NoPlayer.FramesPerSecondChangedListener listener : listeners) {
            listener.onFramesPerSecondChanged(framesPerSecond);
        }
    }
}
