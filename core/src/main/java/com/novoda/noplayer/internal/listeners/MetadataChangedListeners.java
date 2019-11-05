package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.metadata.Metadata;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class MetadataChangedListeners implements NoPlayer.MetadataChangedListener {

    private final Set<NoPlayer.MetadataChangedListener> listeners = new CopyOnWriteArraySet<>();

    public void add(NoPlayer.MetadataChangedListener listener) {
        listeners.add(listener);
    }

    public void remove(NoPlayer.MetadataChangedListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onMetadataChanged(Metadata metadata) {
        for (NoPlayer.MetadataChangedListener listener : listeners) {
            listener.onMetadataChanged(metadata);
        }
    }
}
