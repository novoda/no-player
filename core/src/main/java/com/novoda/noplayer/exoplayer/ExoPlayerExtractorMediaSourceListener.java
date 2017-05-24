package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.source.ExtractorMediaSource;

import java.io.IOException;
import java.util.List;

class ExoPlayerExtractorMediaSourceListener implements ExtractorMediaSource.EventListener {

    private final List<Forwarder> forwarders;

    ExoPlayerExtractorMediaSourceListener(List<Forwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onLoadError(IOException error) {
        for (Forwarder forwarder : forwarders) {
            forwarder.onLoadError(error);
        }
    }
}
