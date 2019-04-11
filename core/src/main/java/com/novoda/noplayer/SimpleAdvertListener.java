package com.novoda.noplayer;

import java.util.List;

public class SimpleAdvertListener implements NoPlayer.AdvertListener {

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
}
