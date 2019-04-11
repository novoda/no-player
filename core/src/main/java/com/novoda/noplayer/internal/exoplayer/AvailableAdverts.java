package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.List;

final class AvailableAdverts {

    private AvailableAdverts() {
        // Utility class.
    }

    // Maps skipped adverts back to available. We may have to change the resume point back to `Played`.
    static AdPlaybackState fromSkipped(long currentPositionInMillis, List<AdvertBreak> advertBreaks, AdPlaybackState adPlaybackState) {
        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            AdPlaybackState.AdGroup adGroup = adPlaybackState.adGroups[i];
            if (advertBreak.startTimeInMillis() >= currentPositionInMillis) {
                for (int stateIndex = 0; stateIndex < adGroup.states.length; stateIndex++) {
                    if (adGroup.states[stateIndex] == AdPlaybackState.AD_STATE_SKIPPED) {
                        adGroup.states[stateIndex] = AdPlaybackState.AD_STATE_AVAILABLE;
                    }
                }
            }
        }
        return adPlaybackState;
    }

}
