package com.novoda.noplayer;

import java.util.List;

public interface AdvertView {

    void setup(List<AdvertBreak> advertBreaks, AdvertInteractionListener advertInteractionListener);

    void removeMarker(AdvertBreak advertBreak);

    interface AdvertInteractionListener {
        void onAdvertClicked();
    }
}
