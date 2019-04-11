package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class PlayedAdverts {

    private PlayedAdverts() {
        // Utility class.
    }

    /**
     * Transforms all adverts before the current player position to the Played state.
     * This is used to represent a resume position, in which a user would already seen the
     * preceding adverts.
     *
     * @param currentPositionInMillis The position before which all adverts will transition from their current state to Played.
     * @param advertBreaks            The client representation of the adverts, our source of truth.
     * @param adPlaybackState         The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the newly played states.
     */
    static AdPlaybackState markAllPastAdvertsAsPlayed(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
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
