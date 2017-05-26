package com.novoda.noplayer.listeners;

import com.novoda.noplayer.exoplayer.forwarder.InfoListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class InfoListeners implements InfoListener {

    private final Set<InfoListener> listeners = new CopyOnWriteArraySet<>();

    public void add(InfoListener listener) {
        listeners.add(listener);
    }

    public void remove(InfoListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onNewInfo(String callingMethod, Map<String, String> keyValuePairs) {
        for (InfoListener listener : listeners) {
            listener.onNewInfo(callingMethod, keyValuePairs);
        }
    }
}
