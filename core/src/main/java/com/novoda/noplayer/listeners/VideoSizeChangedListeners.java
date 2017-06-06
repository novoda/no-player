package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class VideoSizeChangedListeners implements Player.VideoSizeChangedListener {

    private final Set<Player.VideoSizeChangedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.VideoSizeChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.VideoSizeChangedListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (Player.VideoSizeChangedListener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }
}
