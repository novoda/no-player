package com.novoda.noplayer.listeners;

import com.novoda.noplayer.Player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class InfoListeners implements Player.InfoListener {

    private final Set<Player.InfoListener> listeners = new CopyOnWriteArraySet<>();

    public void add(Player.InfoListener listener) {
        listeners.add(listener);
    }

    public void remove(Player.InfoListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onNewInfo(String callingMethod, Map<String, String> callingMethodParams) {
        for (Player.InfoListener listener : listeners) {
            listener.onNewInfo(callingMethod, callingMethodParams);
        }
    }
}
