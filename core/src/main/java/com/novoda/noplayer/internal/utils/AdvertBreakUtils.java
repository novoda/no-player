package com.novoda.noplayer.internal.utils;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

public final class AdvertBreakUtils {

    public static long[] advertOffsets(List<AdvertBreak> advertBreaks) {
        long[] advertOffsets = new long[advertBreaks.size()];
        for (int i = 0; i < advertOffsets.length; i++) {
            advertOffsets[i] = advertBreaks.get(i).startTime();
        }
        return advertOffsets;
    }

    public static long[][] advertBreakDurations(List<AdvertBreak> advertBreaks) {
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaks.size()][];
        for (int i = 0; i < advertBreaks.size(); i++) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();
            long[] advertDurations = new long[adverts.size()];

            for (int j = 0; j < adverts.size(); j++) {
                advertDurations[j] = adverts.get(j).durationInMicros();
            }
            advertBreaksWithAdvertDurations[i] = advertDurations;
        }
        return advertBreaksWithAdvertDurations;
    }

    public static int[] numberOfAdvertsPerAdvertBreak(List<AdvertBreak> advertBreaks) {
        int[] numberOfAdvertsPerBreak = new int[advertBreaks.size()];
        for (int i = 0; i < advertBreaks.size(); i++) {
            List<Advert> adverts = advertBreaks.get(i).adverts();

            numberOfAdvertsPerBreak[i] = adverts.size();
        }

        return numberOfAdvertsPerBreak;
    }

}
