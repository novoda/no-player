package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;

import java.util.HashMap;
import java.util.Map;

class AdGroupFixture {

    private final Map<Integer, Integer> positionStateMapping = new HashMap<>();
    private long[] advertDurations;
    private Uri[] advertUris;
    private int adCount;

    static AdGroupFixture anAdGroup() {
        return new AdGroupFixture();
    }

    AdGroupFixture withAdCount(int adCount) {
        this.adCount = adCount;
        return this;
    }

    AdGroupFixture withAdDurationsUs(long[] advertDurations) {
        this.advertDurations = advertDurations;
        return this;
    }

    AdGroupFixture withAdUris(Uri[] advertUris) {
        this.advertUris = advertUris;
        return this;
    }

    AdGroupFixture withPlayedStateAt(int position) {
        positionStateMapping.put(position, AdPlaybackState.AD_STATE_PLAYED);
        return this;
    }

    AdPlaybackState.AdGroup build() {
        AdPlaybackState.AdGroup group = new AdPlaybackState.AdGroup();
        group = group.withAdCount(adCount);
        group = group.withAdDurationsUs(advertDurations);
        for (int i = 0; i < advertUris.length; i++) {
            Uri uri = advertUris[i];
            group = group.withAdUri(uri, i);
        }
        for (Integer position : positionStateMapping.keySet()) {
            group = group.withAdState(positionStateMapping.get(position), position);
        }
        return group;
    }
}
