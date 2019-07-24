package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.NoPlayer;

import java.util.List;

class AdvertState {

    interface Callback {

        void onAdvertPlayed(int advertBreakIndex, int advertIndex);

        void onAdvertsDisabled();

        void onAdvertsEnabled();

        void onAdvertBreakSkipped(int advertBreakIndex);

        void onAdvertSkipped(int advertBreakIndex, int advertIndex);
    }

    private static final int INVALID_INDEX = -1;

    private final List<AdvertBreak> advertBreaks;
    private final NoPlayer.AdvertListener advertListener;
    private final Callback callback;

    private int advertIndex = -1;
    private int advertBreakIndex = -1;
    private boolean playingAdvert;
    private boolean advertsDisabled;

    AdvertState(List<AdvertBreak> advertBreaks, NoPlayer.AdvertListener advertListener, Callback callback) {
        this.advertBreaks = advertBreaks;
        this.advertListener = advertListener;
        this.callback = callback;
    }

    void update(boolean isPlayingAdvert, int currentAdvertBreakIndex, int currentAdvertIndex) {
        boolean wasPlayingAd = playingAdvert;
        playingAdvert = isPlayingAdvert;

        int previousAdvertBreakIndex = advertBreakIndex;
        int previousAdvertIndex = advertIndex;

        advertBreakIndex = playingAdvert ? currentAdvertBreakIndex : INVALID_INDEX;
        advertIndex = playingAdvert ? currentAdvertIndex : INVALID_INDEX;
        boolean advertFinished = wasPlayingAd && advertIndex != previousAdvertIndex;

        if (advertFinished) {
            callback.onAdvertPlayed(previousAdvertBreakIndex, previousAdvertIndex);
            notifyAdvertEnd(previousAdvertBreakIndex, previousAdvertIndex);
        }

        boolean advertStarted = isPlayingAdvert && advertIndex != previousAdvertIndex;
        if (advertStarted) {
            notifyAdvertStart(advertBreakIndex, advertIndex);
        }
    }

    private void notifyAdvertEnd(int playedAdvertBreakIndex, int playedAdvertIndex) {
        AdvertBreak advertBreak = advertBreaks.get(playedAdvertBreakIndex);
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertEnd(adverts.get(playedAdvertIndex));

        if (advertBreakIndex != playedAdvertBreakIndex) {
            advertListener.onAdvertBreakEnd(advertBreak);
            resetState();
        }
    }

    private void resetState() {
        advertBreakIndex = -1;
        advertIndex = -1;
        playingAdvert = false;
    }

    private void notifyAdvertStart(int advertBreakIndex, int advertIndex) {
        AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);

        if (advertIndex == 0) {
            advertListener.onAdvertBreakStart(advertBreak);
        }

        Advert advert = advertBreak.adverts().get(advertIndex);
        advertListener.onAdvertStart(advert);
    }

    void disableAdverts() {
        if (advertsDisabled) {
            return;
        }

        advertsDisabled = true;
        callback.onAdvertsDisabled();
        advertListener.onAdvertsDisabled();
        resetState();
    }

    void enableAdverts() {
        if (!advertsDisabled) {
            return;
        }

        advertsDisabled = false;
        callback.onAdvertsEnabled();
        advertListener.onAdvertsEnabled(advertBreaks);
        resetState();
    }

    void skipAdvertBreak() {
        if (advertsDisabled || advertBreakIndex < 0) {
            return;
        }

        callback.onAdvertBreakSkipped(advertBreakIndex);
        advertListener.onAdvertBreakSkipped(advertBreaks.get(advertBreakIndex));
        resetState();
    }

    void skipAdvert() {
        if (advertsDisabled || advertBreakIndex < 0 || advertIndex < 0) {
            return;
        }

        callback.onAdvertSkipped(advertBreakIndex, advertIndex);
        AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertSkipped(adverts.get(advertIndex));
        if (advertIndex == adverts.size() - 1) {
            advertListener.onAdvertBreakEnd(advertBreak);
        }
        resetState();
    }

    void clickAdvert() {
        if (advertBreakIndex < 0 || advertIndex < 0) {
            return;
        }

        Advert advert = advertBreaks.get(advertBreakIndex).adverts().get(advertIndex);
        advertListener.onAdvertClicked(advert);
    }

}
