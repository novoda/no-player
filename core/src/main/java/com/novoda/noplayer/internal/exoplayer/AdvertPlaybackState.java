package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AdvertPlaybackState {

    private final AdPlaybackState adPlaybackState;
    private final Map<AdvertBreak, State> advertBreaksByState;

    static AdvertPlaybackState from(List<AdvertBreak> advertBreaks) {
        List<AdvertBreak> sortedAdvertBreaks = sortAdvertBreaksByStartTime(advertBreaks);
        Map<AdvertBreak, State> advertBreaksByState = new HashMap<>();

        long[] advertOffsets = advertBreakOffset(sortedAdvertBreaks);
        AdPlaybackState adPlaybackState = new AdPlaybackState(advertOffsets);

        int advertBreaksCount = sortedAdvertBreaks.size();
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaksCount][];

        for (int i = 0; i < advertBreaksCount; i++) {
            AdvertBreak advertBreak = sortedAdvertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();
            advertBreaksByState.put(advertBreak, State.AVAILABLE);

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
        return new AdvertPlaybackState(adPlaybackState, advertBreaksByState);
    }

    private AdvertPlaybackState(AdPlaybackState adPlaybackState, Map<AdvertBreak, State> advertBreaksByState) {
        this.adPlaybackState = adPlaybackState;
        this.advertBreaksByState = advertBreaksByState;
    }

    AdPlaybackState adPlaybackState() {
        return adPlaybackState;
    }

    Map<AdvertBreak, State> advertBreaksByState() {
        return advertBreaksByState;
    }

    List<AdvertBreak> advertBreaks() {
        return new ArrayList<>(advertBreaksByState.keySet());
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

    enum State {
        AVAILABLE,
        PLAYED,
        SKIPPED
    }

}
