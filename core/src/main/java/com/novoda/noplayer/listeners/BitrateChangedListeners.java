package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.model.Bitrate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class BitrateChangedListeners implements Player.BitrateChangedListener {

    private final Set<Player.BitrateChangedListener> listeners = new CopyOnWriteArraySet<>();

    void add(Player.BitrateChangedListener listener) {
        listeners.add(listener);
    }

    void remove(Player.BitrateChangedListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onBitrateChanged(Bitrate audioBitrate, Bitrate videoBitrate) {
        for (Player.BitrateChangedListener listener : listeners) {
            listener.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
