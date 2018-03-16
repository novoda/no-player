package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.NoPlayer;

class VideoStateChangedForwarder implements VideoRendererEventListener {

    private final NoPlayer.VideoStateChangedListener videoStateChangedListener;

    VideoStateChangedForwarder(NoPlayer.VideoStateChangedListener videoStateChangedListener) {
        this.videoStateChangedListener = videoStateChangedListener;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoStateChangedListener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        // TODO: should we send?
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        // TODO: should we send?
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        // TODO: should we send?
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        videoStateChangedListener.onFramesDropped(count);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        // TODO: should we send?
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        // TODO: should we send?
    }
}
