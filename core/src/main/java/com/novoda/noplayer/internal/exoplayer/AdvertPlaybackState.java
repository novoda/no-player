package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Collections;
import java.util.List;

public final class AdvertPlaybackState {

    private AdvertPlaybackState() {
    }

    public static AdPlaybackState from(List<AdvertBreak> advertBreaks) {
        Collections.sort(advertBreaks, new AdvertBreakStartTimeComparator());

        long[] advertOffsets = advertBreakOffset(advertBreaks);
        AdPlaybackState adPlaybackState = new AdPlaybackState(advertOffsets);

        int advertBreaksCount = advertBreaks.size();
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaksCount][];

        for (int i = 0; i < advertBreaksCount; i++) {
            AdvertBreak advertBreak = advertBreaks.get(i);
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

        return adPlaybackState.withAdDurationsUs(advertBreaksWithAdvertDurations);
    }

    private static long[] advertBreakOffset(List<AdvertBreak> advertBreaks) {
        long[] advertOffsets = new long[advertBreaks.size()];
        for (int i = 0; i < advertOffsets.length; i++) {
            advertOffsets[i] = C.msToUs(advertBreaks.get(i).startTimeInMillis());
        }
        return advertOffsets;
    }

}
