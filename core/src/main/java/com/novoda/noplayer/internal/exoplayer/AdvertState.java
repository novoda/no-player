package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

class AdvertState {

    interface Callback {

        void onAdvertBreakStart(AdvertBreak advertBreak);

        void onAdvertStart(Advert advert);

        void onAdvertEnd(Advert advert);

        void onAdvertBreakEnd(AdvertBreak advertBreak);

        void onAdvertPlayed(int advertBreakIndex, int advertIndex);

        void onAdvertsDisabled();

        void onAdvertsEnabled(List<AdvertBreak> advertBreaks);

        void onAdvertBreakSkipped(int advertBreakIndex, AdvertBreak advertBreak);

        void onAdvertSkipped(int advertBreakIndex, int advertIndex, Advert advert);

        void onAdvertClicked(Advert advert);
    }

    private static final int INVALID_INDEX = -1;

    private final List<AdvertBreak> advertBreaks;
    private final Callback callback;

    private int advertIndex = -1;
    private int advertBreakIndex = -1;
    private boolean playingAdvert;
    private boolean advertsDisabled;

    AdvertState(List<AdvertBreak> advertBreaks, Callback callback) {
        this.advertBreaks = advertBreaks;
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
        callback.onAdvertEnd(adverts.get(playedAdvertIndex));

        if (advertBreakIndex != playedAdvertBreakIndex) {
            callback.onAdvertBreakEnd(advertBreak);
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
            callback.onAdvertBreakStart(advertBreak);
        }

        Advert advert = advertBreak.adverts().get(advertIndex);
        callback.onAdvertStart(advert);
    }

    void disableAdverts() {
        if (advertsDisabled) {
            return;
        }

        advertsDisabled = true;
        callback.onAdvertsDisabled();
        resetState();
    }

    void enableAdverts() {
        if (!advertsDisabled) {
            return;
        }

        advertsDisabled = false;
        callback.onAdvertsEnabled(advertBreaks);
        resetState();
    }

    void skipAdvertBreak() {
        if (advertsDisabled || advertBreakIndex < 0) {
            return;
        }

        callback.onAdvertBreakSkipped(advertBreakIndex, advertBreaks.get(advertBreakIndex));
        resetState();
    }

    void skipAdvert() {
        if (advertsDisabled || advertBreakIndex < 0 || advertIndex < 0) {
            return;
        }

        AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);
        List<Advert> adverts = advertBreak.adverts();
        callback.onAdvertSkipped(advertBreakIndex, advertIndex, adverts.get(advertIndex));
        if (advertIndex == adverts.size() - 1) {
            callback.onAdvertBreakEnd(advertBreak);
        }
        resetState();
    }

    void clickAdvert() {
        if (advertBreakIndex < 0 || advertIndex < 0) {
            return;
        }

        Advert advert = advertBreaks.get(advertBreakIndex).adverts().get(advertIndex);
        callback.onAdvertClicked(advert);
    }

    long advertDurationBy(int advertBreakIndex, int advertIndex) {
        if (advertBreakIndex >= advertBreaks.size()) {
            throw new IllegalStateException("Advert is being played but no data about advert breaks is cached.");
        }

        AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);
        if (advertIndex >= advertBreak.adverts().size()) {
            throw new IllegalStateException("Cached advert break data contains less adverts than current index.");
        }

        return C.msToUs(advertBreak.adverts().get(advertIndex).durationInMillis());
    }
}
