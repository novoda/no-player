package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
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
    public void onAdvertsLoadError(Exception cause) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsLoadError(cause);
        }
    }

    @Override
    public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsLoaded(advertBreaks);
        }
    }

    @Override
    public void onAdvertBreakStart(AdvertBreak advertBreak) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakStart(advertBreak);
        }
    }

    @Override
    public void onAdvertBreakEnd(AdvertBreak advertBreak) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertBreakEnd(advertBreak);
        }
    }

    @Override
    public void onAdvertPrepareError(Advert advert, IOException cause) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertPrepareError(advert, cause);
        }
    }

    @Override
    public void onAdvertStart(Advert advert) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertStart(advert);
        }
    }

    @Override
    public void onAdvertEnd(Advert advert) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertEnd(advert);
        }
    }

    @Override
    public void onAdvertClicked(Advert advert) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertClicked(advert);
        }
    }

    @Override
    public void onAdvertsDisabled() {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsDisabled();
        }
    }

    @Override
    public void onAdvertsEnabled(List<AdvertBreak> advertBreaks) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsEnabled(advertBreaks);
        }
    }

    @Override
    public void onAdvertsSkipped(List<AdvertBreak> advertBreaks) {
        for (NoPlayer.AdvertListener listener : listeners) {
            listener.onAdvertsSkipped(advertBreaks);
        }
    }
}
