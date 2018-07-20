package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DroppedFramesListeners implements NoPlayer.DroppedVideoFramesListener {

    private final Set<NoPlayer.DroppedVideoFramesListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.DroppedVideoFramesListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.DroppedVideoFramesListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onDroppedVideoFrames(int droppedFrames, long elapsedMsSinceLastDroppedFrames) {
        for (NoPlayer.DroppedVideoFramesListener listener : listeners) {
            listener.onDroppedVideoFrames(droppedFrames, elapsedMsSinceLastDroppedFrames);
        }
    }
}
