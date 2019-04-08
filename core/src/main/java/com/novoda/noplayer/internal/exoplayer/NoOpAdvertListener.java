package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertBreakId;
import com.novoda.noplayer.AdvertId;
import com.novoda.noplayer.NoPlayer;

import java.util.List;

enum NoOpAdvertListener implements NoPlayer.AdvertListener {
    INSTANCE;

    @Override
    public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
        // no-op
    }

    @Override
    public void onAdvertBreakStart(AdvertBreakId advertBreakId) {
        // no-op
    }

    @Override
    public void onAdvertBreakEnd(AdvertBreakId advertBreakId) {
        // no-op
    }

    @Override
    public void onAdvertStart(AdvertId advertId) {
        // no-op
    }

    @Override
    public void onAdvertEnd(AdvertId advertId) {
        // no-op
    }

    @Override
    public void onAdvertClicked(Advert advert) {
        // no-op
    }
}
