package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class InfoListeners implements NoPlayer.InfoListener {

    private final Set<NoPlayer.InfoListener> listeners = new CopyOnWriteArraySet<>();

    void add(NoPlayer.InfoListener listener) {
        listeners.add(listener);
    }

    void remove(NoPlayer.InfoListener listener) {
        listeners.remove(listener);
    }

    void clear() {
        listeners.clear();
    }

    @Override
    public void onNewInfo(String callingMethod, Map<String, String> callingMethodParams) {
        for (NoPlayer.InfoListener listener : listeners) {
            listener.onNewInfo(callingMethod, callingMethodParams);
        }
    }
}
