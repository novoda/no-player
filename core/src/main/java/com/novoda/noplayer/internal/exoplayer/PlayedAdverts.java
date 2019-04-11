package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class PlayedAdverts {

    private PlayedAdverts() {
        // Utility class.
    }

    static AdPlaybackState from(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithPlayedAdGroups = adPlaybackState;
        for (int advertBreakIndex = advertBreaks.size() - 1; advertBreakIndex >= 0; advertBreakIndex--) {
            AdvertBreak advertBreak = advertBreaks.get(advertBreakIndex);
            if (advertBreak.startTimeInMillis() >= currentPositionInMillis) {
                continue;
            }

            for (int advertIndex = 0; advertIndex < advertBreak.adverts().size(); advertIndex++) {
                adPlaybackStateWithPlayedAdGroups = adPlaybackStateWithPlayedAdGroups.withPlayedAd(advertBreakIndex, advertIndex);
            }
        }
        return adPlaybackStateWithPlayedAdGroups;
    }

}
