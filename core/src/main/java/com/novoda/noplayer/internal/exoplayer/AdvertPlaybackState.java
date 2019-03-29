package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class AdvertPlaybackState {

    private final AdPlaybackState adPlaybackState;
    private final List<AdvertBreak> advertBreaks;

    static AdvertPlaybackState from(List<AdvertBreak> advertBreaks) {
        List<AdvertBreak> sortedAdvertBreaks = sortAdvertBreaksByStartTime(advertBreaks);

        long[] advertOffsets = advertBreakOffset(sortedAdvertBreaks);
        AdPlaybackState adPlaybackState = new AdPlaybackState(advertOffsets);

        int advertBreaksCount = sortedAdvertBreaks.size();
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaksCount][];

        for (int i = 0; i < advertBreaksCount; i++) {
            AdvertBreak advertBreak = sortedAdvertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();

            int advertsCount = adverts.size();
            adPlaybackState = adPlaybackState.withAdCount(i, advertsCount);

            long[] advertDurations = new long[advertsCount];

            for (int j = 0; j < advertsCount; j++) {
                Advert advert = adverts.get(j);
                advertDurations[j] = C.msToUs(advert.durationInMillis());
                adPlaybackState = adPlaybackState.withAdUri(i, j, advert.uri());
            }

            advertBreaksWithAdvertDurations[i] = advertDurations;
        }

        adPlaybackState = adPlaybackState.withAdDurationsUs(advertBreaksWithAdvertDurations);
        return new AdvertPlaybackState(adPlaybackState, sortedAdvertBreaks);
    }

    private AdvertPlaybackState(AdPlaybackState adPlaybackState, List<AdvertBreak> advertBreaks) {
        this.adPlaybackState = adPlaybackState;
        this.advertBreaks = advertBreaks;
    }

    AdPlaybackState adPlaybackState() {
        return adPlaybackState;
    }

    List<AdvertBreak> advertBreaks() {
        return advertBreaks;
    }

    private static List<AdvertBreak> sortAdvertBreaksByStartTime(List<AdvertBreak> advertBreaks) {
        List<AdvertBreak> sortedAdvertBreaks = new ArrayList<>(advertBreaks);
        Collections.sort(sortedAdvertBreaks, new AdvertBreakStartTimeComparator());
        return sortedAdvertBreaks;
    }

    private static long[] advertBreakOffset(List<AdvertBreak> advertBreaks) {
        long[] advertOffsets = new long[advertBreaks.size()];
        for (int i = 0; i < advertOffsets.length; i++) {
            advertOffsets[i] = C.msToUs(advertBreaks.get(i).startTimeInMillis());
        }
        return advertOffsets;
    }

}
