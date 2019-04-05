package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class SkippedAdverts {

    static AdPlaybackState from(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            if (advertBreak.startTimeInMillis() >= currentPositionInMillis) {
                continue;
            }

            adPlaybackState = adPlaybackState.withSkippedAdGroup(i);
        }
        return adPlaybackState;
    }

}
