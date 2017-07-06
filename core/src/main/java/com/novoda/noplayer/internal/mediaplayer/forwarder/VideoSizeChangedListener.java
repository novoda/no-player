package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VideoSizeChangedListener implements MediaPlayer.OnVideoSizeChangedListener {

    private final List<MediaPlayer.OnVideoSizeChangedListener> listeners = new CopyOnWriteArrayList<>();

    public void add(MediaPlayer.OnVideoSizeChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        for (MediaPlayer.OnVideoSizeChangedListener listener : listeners) {
            listener.onVideoSizeChanged(mp, width, height);
        }
    }
}
