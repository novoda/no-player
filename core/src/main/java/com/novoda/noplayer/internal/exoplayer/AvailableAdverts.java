package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class AvailableAdverts {

    private AvailableAdverts() {
        // Utility class.
    }

    /**
     * Transforms all skipped adverts to available adverts.
     * <p>
     * WARNING: This relies on mutation to achieve what would not be possible
     * as the exo-player {@link AdPlaybackState} does not allow transitioning from
     * Skipped to Available adverts.
     *
     * @param advertBreaks    The client representation of the adverts, our source of truth.
     * @param adPlaybackState The {@link AdPlaybackState} to mutate with the new states.
     */
    static void markSkippedAdvertsAsAvailable(List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            AdPlaybackState.AdGroup adGroup = adPlaybackState.adGroups[i];
            for (int stateIndex = 0; stateIndex < adGroup.states.length; stateIndex++) {
                if (adGroup.states[stateIndex] == AdPlaybackState.AD_STATE_SKIPPED) {
                    adGroup.states[stateIndex] = AdPlaybackState.AD_STATE_AVAILABLE;
                }
            }
        }
    }

}
