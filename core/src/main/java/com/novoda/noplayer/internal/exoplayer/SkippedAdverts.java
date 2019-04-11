package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class SkippedAdverts {

    private SkippedAdverts() {
        // Utility class.
    }

    /**
     * Transforms all adverts that are not currently Played to Skipped.
     *
     * @param advertBreaks    The client representation of the adverts, our source of truth.
     * @param adPlaybackState The {@link AdPlaybackState} to mutate with the new states.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState from(List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withSkippedAdGroup(i);
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

}
