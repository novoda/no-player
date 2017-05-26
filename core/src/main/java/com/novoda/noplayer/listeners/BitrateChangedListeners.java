package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.forwarder.Bitrate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BitrateChangedListeners implements Player.BitrateChangedListener {

    private final Set<Player.BitrateChangedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.BitrateChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.BitrateChangedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onBitrateChanged(Bitrate audioBitrate, Bitrate videoBitrate) {
        for (Player.BitrateChangedListener listener : listeners) {
            listener.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
