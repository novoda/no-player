package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class PlayedAdverts {

    private PlayedAdverts() {
        // Utility class.
    }

    static AdPlaybackState from(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        int numberOfAdvertBreaks = advertBreaks.size() - 1;
        for (int advertBreakIndex = numberOfAdvertBreaks; advertBreakIndex >= 0; advertBreakIndex--) {
            AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);
            if (advertBreak.startTimeInMillis() >= currentPositionInMillis) {
                continue;
            }

            int numberOfAdverts = advertBreak.adverts().size();
            for (int advertIndex = 0; advertIndex < numberOfAdverts; advertIndex++) {
                adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withPlayedAd(advertBreakIndex, advertIndex);
            }
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

}
