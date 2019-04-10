package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

// TODO: This should be played not skipped. Although they are probably the same really.
final class SkippedAdverts {

    private SkippedAdverts() {
        // Utility class.
    }

    static AdPlaybackState from(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            if (advertBreak.startTimeInMillis() >= currentPositionInMillis) {
                continue;
            }

            adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withSkippedAdGroup(i); // TODO: This should be played. 
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

}
