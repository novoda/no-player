package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertView;

import java.util.List;

enum NoOpAdvertView implements AdvertView {
    INSTANCE;

    @Override
    public void setup(List<AdvertBreak> advertBreaks, AdvertInteractionListener advertInteractionListener) {
        // no-op
    }

    @Override
    public void removeMarker(AdvertBreak advertBreak) {
        // no-op
    }
}
