package com.novoda.noplayer;

public interface AdvertView {

    NoPlayer.AdvertListener getAdvertListener();

    void attach(AdvertInteractionListener advertInteractionListener);

    void detach(AdvertInteractionListener advertInteractionListener);

    interface AdvertInteractionListener {
        void onAdvertClicked();
    }
}
