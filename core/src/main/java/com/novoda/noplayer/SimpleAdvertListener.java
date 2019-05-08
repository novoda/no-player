package com.novoda.noplayer;

import java.io.IOException;
import java.util.List;

public class SimpleAdvertListener implements NoPlayer.AdvertListener {

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
    public void onAdvertsSkipped(List<AdvertBreak> advertBreaks) {
        // no op
    }
}
