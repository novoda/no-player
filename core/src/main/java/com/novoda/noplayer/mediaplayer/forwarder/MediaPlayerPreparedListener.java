package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaPlayerPreparedListener implements MediaPlayer.OnPreparedListener {

    private final List<MediaPlayer.OnPreparedListener> listeners = new CopyOnWriteArrayList<>();

    public void add(MediaPlayer.OnPreparedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        for (MediaPlayer.OnPreparedListener listener : listeners) {
            listener.onPrepared(mp);
        }
    }
}
