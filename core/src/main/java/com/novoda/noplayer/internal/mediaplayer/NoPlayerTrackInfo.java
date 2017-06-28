package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

class NoPlayerTrackInfo {

    private final MediaPlayer.TrackInfo trackInfo;

    NoPlayerTrackInfo(MediaPlayer.TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    int type() {
        return trackInfo.getTrackType();
    }

    String language() {
        return trackInfo.getLanguage();
    }
}
