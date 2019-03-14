package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Collections;
import java.util.List;

public final class AdvertPlaybackState {

    private AdvertPlaybackState() {
    }

    public static AdPlaybackState from(List<AdvertBreak> advertBreaks) {
        Collections.sort(advertBreaks, new AdvertBreakStartTimeComparer());

        long[] advertOffsets = advertBreakOffset(advertBreaks);
        AdPlaybackState adPlaybackState = new AdPlaybackState(advertOffsets);

        long[][] advertBreaksWithAdvertDurations = new long[advertBreaks.size()][];

        for (int i = 0; i < advertBreaks.size(); i++) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();

            long[] advertDurations = new long[adverts.size()];

            adPlaybackState = adPlaybackState.withAdCount(i, adverts.size());

            for (int j = 0; j < adverts.size(); j++) {
                Advert advert = adverts.get(j);
                advertDurations[j] = advert.durationInMicros();
                adPlaybackState = adPlaybackState.withAdUri(i, j, advert.uri());
            }

            advertBreaksWithAdvertDurations[i] = advertDurations;
        }

        return adPlaybackState.withAdDurationsUs(advertBreaksWithAdvertDurations);
    }

    private static long[] advertBreakOffset(List<AdvertBreak> advertBreaks) {
        long[] advertOffsets = new long[advertBreaks.size()];
        for (int i = 0; i < advertOffsets.length; i++) {
            advertOffsets[i] = advertBreaks.get(i).startTime();
        }
        return advertOffsets;
    }

}
