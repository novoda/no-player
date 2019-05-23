package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

/**
 * This is not the traditional usage of skipped. This handles the scenario where the client
 * wants to hide adverts without forcing the whole advert load process again.
 * <p>
 * If and when we tackle skipping of adverts we will need to revisit this flow.
 */
final class SkippedAdverts {

    private SkippedAdverts() {
        // Utility class.
    }

    /**
     * Transforms all adverts that are not currently Played to Skipped.
     *
     * @param advertBreaks    The client representation of the adverts, our source of truth.
     * @param adPlaybackState The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState markAllNonPlayedAdvertsAsSkipped(List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withSkippedAdGroup(i);
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

    /**
     * Transforms all available adverts before a given position to skipped adverts.
     *
     * @param currentPositionInMillis The position before which all adverts will transition from Available to Skipped.
     * @param advertBreaks            The client representation of the adverts, our source of truth.
     * @param adPlaybackState         The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState markAllPastAvailableAdvertsAsSkipped(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            AdPlaybackState.AdGroup adGroup = adPlaybackState.adGroups[i];
            if (advertBreak.startTimeInMillis() < currentPositionInMillis && adGroup.hasUnplayedAds()) {
                adPlaybackStateWithSkippedAdGroups = adPlaybackState.withSkippedAdGroup(i);
            }
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

}
