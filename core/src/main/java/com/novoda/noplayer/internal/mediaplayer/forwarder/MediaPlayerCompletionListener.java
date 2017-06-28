package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class MediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {

    private final List<MediaPlayer.OnCompletionListener> listeners = new CopyOnWriteArrayList<>();

    void add(MediaPlayer.OnCompletionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        for (MediaPlayer.OnCompletionListener listener : listeners) {
            listener.onCompletion(mp);
        }
    }
}
