package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener {

    private final List<MediaPlayer.OnErrorListener> listeners = new CopyOnWriteArrayList<>();

    void add(MediaPlayer.OnErrorListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        boolean handled = false;
        for (MediaPlayer.OnErrorListener listener : listeners) {
            handled = listener.onError(mp, what, extra) || handled;
        }
        return handled;
    }
}
