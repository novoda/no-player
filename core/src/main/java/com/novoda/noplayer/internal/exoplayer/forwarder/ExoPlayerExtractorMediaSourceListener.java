package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerExtractorMediaSourceListener implements ExtractorMediaSource.EventListener {

    private final List<ExtractorMediaSource.EventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(ExtractorMediaSource.EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onLoadError(IOException error) {
        for (ExtractorMediaSource.EventListener listener : listeners) {
            listener.onLoadError(error);
        }
    }
}
