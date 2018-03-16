package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class VideoStateChangedListeners implements NoPlayer.VideoStateChangedListener {

    private final Set<NoPlayer.VideoStateChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.VideoStateChangedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.VideoStateChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (NoPlayer.VideoStateChangedListener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onFramesDropped(int numberOfFramesDropped) {
        for (NoPlayer.VideoStateChangedListener listener : listeners) {
            listener.onFramesDropped(numberOfFramesDropped);
        }
    }
}
