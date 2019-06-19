package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.NoPlayer;

import java.util.List;

class AdvertState {

    interface Callback {

        void onAdvertPlayed(int advertBreakIndex, int advertIndex);

    }

    private boolean playingAdvert;
    private int advertIndex = -1;
    private int advertBreakIndex = -1;
    private List<AdvertBreak> advertBreaks;
    private NoPlayer.AdvertListener advertListener;

    AdvertState(List<AdvertBreak> advertBreaks, NoPlayer.AdvertListener advertListener) {
        this.advertBreaks = advertBreaks;
        this.advertListener = advertListener;
    }

    void update(boolean isPlayingAdvert, int currentAdvertBreakIndex, int currentAdvertIndex, Callback callback) {
        boolean wasPlayingAdvert = playingAdvert;
        playingAdvert = isPlayingAdvert;

        int previousAdvertBreakIndex = advertBreakIndex;
        int previousAdvertIndex = advertIndex;

        advertBreakIndex = playingAdvert ? currentAdvertBreakIndex : C.INDEX_UNSET;
        advertIndex = playingAdvert ? currentAdvertIndex : C.INDEX_UNSET;
        boolean advertFinished = wasPlayingAdvert && advertIndex != previousAdvertIndex;

        if (advertFinished) {
            callback.onAdvertPlayed(previousAdvertBreakIndex, previousAdvertIndex);
            notifyAdvertEnd(previousAdvertBreakIndex, previousAdvertIndex);
        }

        if (playingAdvert && advertIndex != previousAdvertIndex) {
            notifyAdvertStart(advertBreaks.get(advertBreakIndex));
        }
    }

    private void notifyAdvertStart(AdvertBreak advertBreak) {
        if (advertIndex == 0) {
            advertListener.onAdvertBreakStart(advertBreak);
        }

        Advert advert = advertBreak.adverts().get(advertIndex);
        advertListener.onAdvertStart(advert);
    }

    private void notifyAdvertEnd(int playedAdvertGroupIndex, int playedAdvertIndexInAdvertGroup) {
        AdvertBreak advertBreak = advertBreaks.get(playedAdvertGroupIndex);
        List<Advert> adverts = advertBreak.adverts();
        advertListener.onAdvertEnd(adverts.get(playedAdvertIndexInAdvertGroup));

        if (advertBreakIndex != playedAdvertGroupIndex) {
            advertListener.onAdvertBreakEnd(advertBreak);
            resetState();
        }
    }

    private void resetState() {
        advertBreakIndex = -1;
        advertIndex = -1;
        playingAdvert = false;
    }

}
