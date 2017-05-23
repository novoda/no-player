package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.novoda.noplayer.listeners.BitrateChangedListeners;

class BitrateForwarder implements ExoPlayerTwoFacade.BitrateForwarder {

    private Bitrate videoBitrate = Bitrate.fromBitsPerSecond(0);
    private Bitrate audioBitrate = Bitrate.fromBitsPerSecond(0);

    private final BitrateChangedListeners bitrateChangedListeners;

    BitrateForwarder(BitrateChangedListeners bitrateChangedListeners) {
        this.bitrateChangedListeners = bitrateChangedListeners;
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
        if (trackType == C.TRACK_TYPE_VIDEO) {
            videoBitrate = Bitrate.fromBitsPerSecond(trackFormat.bitrate);
            bitrateChangedListeners.onBitrateChanged(audioBitrate, videoBitrate);
        } else if (trackType == C.TRACK_TYPE_AUDIO) {
            audioBitrate = Bitrate.fromBitsPerSecond(trackFormat.bitrate);
            bitrateChangedListeners.onBitrateChanged(audioBitrate, videoBitrate);
        }
    }
}
