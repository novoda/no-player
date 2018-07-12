package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.video.VideoListener;
import com.novoda.noplayer.NoPlayer;

class VideoSizeChangedForwarder implements VideoListener {

    private final NoPlayer.VideoSizeChangedListener videoSizeChangedListener;

    VideoSizeChangedForwarder(NoPlayer.VideoSizeChangedListener videoSizeChangedListener) {
        this.videoSizeChangedListener = videoSizeChangedListener;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoSizeChangedListener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
    }

    @Override
    public void onRenderedFirstFrame() {
        // TODO: should we send?
    }
}
