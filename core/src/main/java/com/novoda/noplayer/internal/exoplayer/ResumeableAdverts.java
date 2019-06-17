package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;

final class ResumeableAdverts {

    private ResumeableAdverts() {
        // Utility class.
    }

    static AdPlaybackState markAsResumeableFrom(AdPlaybackState adPlaybackState, long positionInMillis) {
        if (adPlaybackState.adGroupCount <= 0 || adPlaybackState.adGroups[0].count <= 0) {
            return adPlaybackState;
        }
        long groupResumePosition = C.msToUs(positionInMillis);
        AdPlaybackState.AdGroup firstAdGroup = adPlaybackState.adGroups[0];

        AdPlaybackState updatedState = adPlaybackState;
        long playedAdvertDuration = 0;
        for (int index = 0; index < firstAdGroup.count; index++) {
            long durationWithCurrentAd = playedAdvertDuration + firstAdGroup.durationsUs[index];
            if (durationWithCurrentAd <= groupResumePosition) {
                updatedState = updatedState.withPlayedAd(0, index);
                playedAdvertDuration += firstAdGroup.durationsUs[index];
            }
            if (groupResumePosition <= playedAdvertDuration) {
                break;
            }

        }
        if (updatedState.adGroups[0].hasUnplayedAds()) {
            updatedState = updatedState.withAdResumePositionUs(groupResumePosition - playedAdvertDuration);
        }

        return updatedState;
    }

}
