package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

import java.util.HashMap;

class VideoSizeChangedInfoForwarder implements MediaPlayer.OnVideoSizeChangedListener {

    private final Player.InfoListener infoListener;

    VideoSizeChangedInfoForwarder(Player.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        HashMap<String, String> callingMethodParameters = new HashMap<>();

        callingMethodParameters.put("mp", String.valueOf(mp));
        callingMethodParameters.put("width", String.valueOf(width));
        callingMethodParameters.put("height", String.valueOf(height));

        infoListener.onNewInfo("onVideoSizeChanged", callingMethodParameters);
    }
}
