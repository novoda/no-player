package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
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
    static AdPlaybackState markAdvertBreakAsSkipped(List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        AdPlaybackState adPlaybackStateWithSkippedAdGroups = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            adPlaybackStateWithSkippedAdGroups = adPlaybackStateWithSkippedAdGroups.withSkippedAdGroup(i);
        }
        return adPlaybackStateWithSkippedAdGroups;
    }

    /**
     * Transforms all adverts in the given break that are not currently Played to Skipped.
     *
     * @param adGroupIndex    The index of the advert break inside the group to be skipped
     * @param adPlaybackState The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState markAdvertBreakAsSkipped(int adGroupIndex, AdPlaybackState adPlaybackState) {
        return adPlaybackState.withSkippedAdGroup(adGroupIndex);
    }

    /**
     * Transforms all adverts in the given break that are not currently Played to Skipped.
     *
     * @param adIndexInAdGroup The index of the advert inside the break to be skipped
     * @param adGroupIndex     The index of the advert break inside the group to be skipped
     * @param adPlaybackState  The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState markAdvertAsSkipped(int adIndexInAdGroup, int adGroupIndex, AdPlaybackState adPlaybackState) {
        return adPlaybackState.withSkippedAd(adGroupIndex, adIndexInAdGroup);
    }

    /**
     * Transforms all available adverts before a given position to skipped adverts.
     *
     * @param currentPositionInMillis The position before which all adverts will transition from Available to Skipped.
     * @param advertBreaks            The client representation of the adverts, our source of truth.
     * @param adPlaybackState         The {@link AdPlaybackState} to alter advert state on.
     * @return The {@link AdPlaybackState} with the new Skipped states.
     */
    static AdPlaybackState markAllPastAvailableAdvertsAsSkipped(long currentPositionInMillis,
                                                                List<AdvertBreak> advertBreaks,
                                                                AdPlaybackState adPlaybackState) {
        AdPlaybackState updatedPlaybackState = adPlaybackState;
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            AdPlaybackState.AdGroup adGroup = updatedPlaybackState.adGroups[i];
            if (advertBreak.startTimeInMillis() < currentPositionInMillis && adGroup.hasUnplayedAds()) {
                updatedPlaybackState = updatedPlaybackState.withSkippedAdGroup(i);
            }
        }
        return updatedPlaybackState;
    }

    static AdPlaybackState markCurrentGateAsSkipped(long currentPositionInMillis,
                                                    List<AdvertBreak> advertBreaks,
                                                    AdPlaybackState adPlaybackState) {

        int advertBreakIndex = C.INDEX_UNSET;
        for (int i = 0; i < advertBreaks.size(); i++) {
            if (advertBreaks.get(i).startTimeInMillis() <= currentPositionInMillis) {
                advertBreakIndex = i;
            } else {
                break;
            }
        }

        if (advertBreakIndex == C.INDEX_UNSET) {
            return adPlaybackState;
        } else {
            return adPlaybackState.withSkippedAdGroup(advertBreakIndex);
        }
    }

}
