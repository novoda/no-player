package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class VideoSizeChangedListeners implements NoPlayer.VideoSizeChangedListener {

    private final Set<NoPlayer.VideoSizeChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.VideoSizeChangedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.VideoSizeChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (NoPlayer.VideoSizeChangedListener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }
}
