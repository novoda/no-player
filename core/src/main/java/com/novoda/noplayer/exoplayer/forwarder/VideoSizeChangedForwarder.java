package com.novoda.noplayer.exoplayer.forwarder;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

class VideoSizeChangedForwarder implements VideoRendererEventListener {

    private final VideoSizeChangedListeners videoSizeChangedListeners;

    VideoSizeChangedForwarder(VideoSizeChangedListeners videoSizeChangedListeners) {
        this.videoSizeChangedListeners = videoSizeChangedListeners;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoSizeChangedListeners.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        //TODO should we send ?
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        //TODO should we send ?
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        //TODO should we send ?
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        //TODO should we send ?
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        //TODO should we send ?
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        //TODO should we send ?
    }
}
