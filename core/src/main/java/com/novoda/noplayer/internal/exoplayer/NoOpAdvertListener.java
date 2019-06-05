package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.NoPlayer;

import java.io.IOException;
import java.util.List;

enum NoOpAdvertListener implements NoPlayer.AdvertListener {
    INSTANCE;

    @Override
    public void onAdvertsLoadError(Exception cause) {
        // no-op
    }

    @Override
    public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
        // no-op
    }

    @Override
    public void onAdvertBreakStart(AdvertBreak advertBreak) {
        // no-op
    }

    @Override
    public void onAdvertBreakEnd(AdvertBreak advertBreak) {
        // no-op
    }

    @Override
    public void onAdvertPrepareError(Advert advert, IOException cause) {
        // no-op
    }

    @Override
    public void onAdvertStart(Advert advert) {
        // no-op
    }

    @Override
    public void onAdvertEnd(Advert advert) {
        // no-op
    }

    @Override
    public void onAdvertClicked(Advert advert) {
        // no-op
    }

    @Override
    public void onAdvertsDisabled() {
        // no-op
    }

    @Override
    public void onAdvertsEnabled(List<AdvertBreak> advertBreaks) {
        // no-op
    }

    @Override
    public void onAdvertsSkipped(AdvertBreak advertBreak) {
        // no-op
    }

}
