package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class SkippedAdverts {

    private SkippedAdverts() {
        // Utility class.
    }

    static AdPlaybackState from(List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withSkippedAdGroup(i);
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

}
