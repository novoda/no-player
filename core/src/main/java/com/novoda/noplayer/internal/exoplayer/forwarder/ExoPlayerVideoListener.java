package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ExoPlayerVideoListener implements VideoListener {

    private final List<VideoListener> listeners = new CopyOnWriteArrayList<>();

    public void add(VideoListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (VideoListener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame() {
        for (VideoListener listener : listeners) {
            listener.onRenderedFirstFrame();
        }
    }
}
