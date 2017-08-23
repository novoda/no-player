package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.model.Bitrate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class BitrateChangedListeners implements NoPlayer.BitrateChangedListener {

    private final Set<NoPlayer.BitrateChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.BitrateChangedListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.BitrateChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onBitrateChanged(Bitrate audioBitrate, Bitrate videoBitrate) {
        for (NoPlayer.BitrateChangedListener listener : listeners) {
            listener.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
