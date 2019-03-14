package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.AdvertBreak;

import java.io.Serializable;
import java.util.Comparator;

class AdvertBreakStartTimeComparer implements Comparator<AdvertBreak>, Serializable {

    @Override
    public int compare(AdvertBreak o1, AdvertBreak o2) {
        return compareLong(o1.startTime(), o2.startTime());
    }

    private static int compareLong(long x, long y) {
        if (x < y) {
            return -1;
        } else {
            return (x == y) ? 0 : 1;
        }
    }
}
