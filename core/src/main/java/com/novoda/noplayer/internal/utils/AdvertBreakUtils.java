package com.novoda.noplayer.internal.utils;

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

}
