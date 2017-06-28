package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.view.Surface;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerVideoRendererEventListener implements VideoRendererEventListener {

    private final List<VideoRendererEventListener> listeners = new CopyOnWriteArrayList<>();

    public void add(VideoRendererEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onVideoEnabled(counters);
        }
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onVideoDecoderInitialized(decoderName, initializedTimestampMs, initializationDurationMs);
        }
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onVideoInputFormatChanged(format);
        }
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onDroppedFrames(count, elapsedMs);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onRenderedFirstFrame(surface);
        }
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        for (VideoRendererEventListener listener : listeners) {
            listener.onVideoDisabled(counters);
        }
    }
}
