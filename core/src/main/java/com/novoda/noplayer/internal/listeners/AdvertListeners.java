package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertBreakId;
import com.novoda.noplayer.AdvertId;
import com.novoda.noplayer.NoPlayer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class AdvertListeners implements NoPlayer.AdvertListener {

    private final Set<NoPlayer.AdvertListener> listeners = new CopyOnWriteArraySet<>();

    public void add(NoPlayer.AdvertListener listener) {
        listeners.add(listener);
    }

    public void remove(NoPlayer.AdvertListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    @Override
    public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsLoaded(advertBreaks);
        }
    }

    @Override
    public void onAdvertBreakStart(AdvertBreakId advertBreakId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakStart(advertBreakId);
        }
    }

    @Override
    public void onAdvertBreakEnd(AdvertBreakId advertBreakId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakEnd(advertBreakId);
        }
    }

    @Override
    public void onAdvertStart(AdvertId advertId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertStart(advertId);
        }
    }

    @Override
    public void onAdvertEnd(AdvertId advertId) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertEnd(advertId);
        }
    }

    @Override
    public void onAdvertClicked(Advert advert) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertClicked(advert);
        }
    }
}
