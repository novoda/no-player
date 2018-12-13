package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

class BandwidthMeterCreator {
    private final Context context;

    BandwidthMeterCreator(Context context) {
        this.context = context;
    }

    DefaultBandwidthMeter create(long maxInitialBitrate) {
        return new DefaultBandwidthMeter.Builder(context)
                .setInitialBitrateEstimate(maxInitialBitrate)
                .build();
    }
}
