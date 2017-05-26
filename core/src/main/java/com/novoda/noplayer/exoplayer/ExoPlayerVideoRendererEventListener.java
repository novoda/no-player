package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

class ExoPlayerVideoRendererEventListener implements VideoRendererEventListener {

    private final List<ExoPlayerForwarder> forwarders;

    private int videoWidth;
    private int videoHeight;

    ExoPlayerVideoRendererEventListener(List<ExoPlayerForwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        // no-op
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        // no-op
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        // no-op
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onDroppedFrames(count, elapsedMs);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoHeight = height;
        videoWidth = width;
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.forwardVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        // no-op
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        // no-op
    }

    public int videoWidth() {
        return videoWidth;
    }

    public int videoHeight() {
        return videoHeight;
    }
}
