package com.novoda.noplayer.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.listeners.InfoListeners;

import java.util.HashMap;

class VideoSizeChangedInfoForwarder implements MediaPlayer.OnVideoSizeChangedListener {

    private final InfoListeners infoListeners;

    public VideoSizeChangedInfoForwarder(InfoListeners infoListeners) {
        this.infoListeners = infoListeners;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        HashMap<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("mp", String.valueOf(mp));
        keyValuePairs.put("width", String.valueOf(width));
        keyValuePairs.put("height", String.valueOf(height));

        infoListeners.onNewInfo("onVideoSizeChanged", keyValuePairs);
    }
}
