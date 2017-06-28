package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.utils.NoPlayerLog;

class VideoSizeChangedForwarder implements MediaPlayer.OnVideoSizeChangedListener {

    private final VideoSizeChangedListeners videoSizeChangedListeners;

    private int previousWidth;
    private int previousHeight;

    VideoSizeChangedForwarder(VideoSizeChangedListeners videoSizeChangedListeners) {
        this.videoSizeChangedListeners = videoSizeChangedListeners;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (bothDimensionsHaveChanged(width, height)) {
            videoSizeChangedListeners.onVideoSizeChanged(width, height, 0, 1);
        } else {
            NoPlayerLog.w("Video size changed but we have swallowed the event due to only 1 dimension changing");
        }
        previousWidth = width;
        previousHeight = height;
    }

    private boolean bothDimensionsHaveChanged(int width, int height) {
        boolean widthHasChanged = width != previousWidth;
        boolean heightHasChanged = height != previousHeight;
        return widthHasChanged && heightHasChanged;
    }
}
