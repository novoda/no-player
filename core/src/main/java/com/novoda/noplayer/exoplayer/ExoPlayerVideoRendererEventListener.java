package com.novoda.noplayer.exoplayer;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

class ExoPlayerVideoRendererEventListener implements VideoRendererEventListener {

    private final List<ExoPlayerForwarder> forwarders;

    private int videoWidth;  //TODO remove state
    private int videoHeight;

    ExoPlayerVideoRendererEventListener(List<ExoPlayerForwarder> forwarders) {
        this.forwarders = forwarders;
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onVideoEnabled(counters);
        }
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onVideoDecoderInitialized(decoderName, initializedTimestampMs, initializationDurationMs);
        }
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onVideoInputFormatChanged(format);
        }
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
            forwarder.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onRenderedFirstFrame(surface);
        }
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        for (ExoPlayerForwarder forwarder : forwarders) {
            forwarder.onVideoDisabled(counters);
        }
    }

    public int videoWidth() {
        return videoWidth;
    }

    public int videoHeight() {
        return videoHeight;
    }
}
