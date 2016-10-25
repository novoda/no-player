package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.notils.logger.simple.Log;

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
            Log.w("Video size changed but we have swallowed the event due to only 1 dimension changing");
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
