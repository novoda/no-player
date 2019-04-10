package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.ArrayList;
import java.util.List;

final class AdvertPlaybackStateByState {

    private AdvertPlaybackStateByState() {
        // Utility class.
    }

    // Two layers
    // Skipped Adverts changes the `AdvertBreakState`
    // This class modifies the `AdPlaybackState`, perhaps we can move all modifications here and test?

    static List<AdvertPlaybackState.AdvertBreakState> from(long playbackPositionInMillis,
                                                           List<AdvertBreak> advertBreaks,
                                                           List<AdvertPlaybackState.AdvertBreakState> advertBreakStates) {
        List<AdvertPlaybackState.AdvertBreakState> advertBreakStatesCopy = new ArrayList<>(advertBreakStates);

        for (int i = advertBreaks.size() - 1; i >= 0; i--) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            if (advertBreak.startTimeInMillis() >= playbackPositionInMillis) {
                continue;
            }
            advertBreakStatesCopy.set(i, AdvertPlaybackState.AdvertBreakState.SKIPPED);
        }

        return advertBreakStatesCopy;
    }

    static AdPlaybackState from(AdPlaybackState adPlaybackState,
                                List<AdvertBreak> advertBreaks,
                                List<AdvertPlaybackState.AdvertBreakState> advertBreakStates) {
        AdPlaybackState modifiedAdPlaybackState = adPlaybackState;
        int numberOfAdvertBreaks = advertBreaks.size();

        for (int advertBreakIndex = 0; advertBreakIndex < numberOfAdvertBreaks; advertBreakIndex++) {
            AdvertPlaybackState.AdvertBreakState advertBreakState = advertBreakStates.get(advertBreakIndex);
            switch (advertBreakState) {
                case PLAYED:
                    modifiedAdPlaybackState = markAsPlayed(modifiedAdPlaybackState, advertBreakIndex);
                    break;
                case SKIPPED:
                    modifiedAdPlaybackState = modifiedAdPlaybackState.withSkippedAdGroup(advertBreakIndex);
                    break;
                default:
                    break;
            }
        }
        return modifiedAdPlaybackState;
    }

    private static AdPlaybackState markAsPlayed(AdPlaybackState adPlaybackState, int advertBreakIndex) {
        AdPlaybackState modifiedAdPlaybackState = adPlaybackState;
        int numberOfAdverts = modifiedAdPlaybackState.adGroups[advertBreakIndex].count;

        for (int advertIndex = 0; advertIndex < numberOfAdverts; advertIndex++) {
            modifiedAdPlaybackState = modifiedAdPlaybackState.withPlayedAd(advertBreakIndex, advertIndex);
        }

        return modifiedAdPlaybackState;
    }

}
