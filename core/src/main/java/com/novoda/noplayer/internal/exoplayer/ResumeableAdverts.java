package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;

final class ResumeableAdverts {

    private ResumeableAdverts() {
        // Utility class.
    }

    static AdPlaybackState markAsResumeableFrom(AdPlaybackState adPlaybackState, long advertResumePositionInMillis, long playbackPositionInMillis) {
        int adGroupIndex = adPlaybackState.getAdGroupIndexForPositionUs(C.msToUs(playbackPositionInMillis));

        if (adGroupIndex == C.INDEX_UNSET || adPlaybackState.adGroupCount <= 0 || adPlaybackState.adGroups[adGroupIndex].count <= 0) {
            return adPlaybackState;
        }
        long groupResumePosition = C.msToUs(advertResumePositionInMillis);
        AdPlaybackState.AdGroup advertGroup = adPlaybackState.adGroups[adGroupIndex];

        AdPlaybackState updatedState = adPlaybackState;
        long playedAdvertDuration = 0;
        for (int index = 0; index < advertGroup.count; index++) {
            long durationWithCurrentAd = playedAdvertDuration + advertGroup.durationsUs[index];
            if (groupResumePosition >= durationWithCurrentAd) {
                updatedState = updatedState.withPlayedAd(adGroupIndex, index);
                playedAdvertDuration += advertGroup.durationsUs[index];
            } else {
                break;
            }
        }
        if (updatedState.adGroups[adGroupIndex].hasUnplayedAds()) {
            updatedState = updatedState.withAdResumePositionUs(groupResumePosition - playedAdvertDuration);
        }

        return updatedState;
    }

}
