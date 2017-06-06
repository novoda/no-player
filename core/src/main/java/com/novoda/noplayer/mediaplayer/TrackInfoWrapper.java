package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

class TrackInfoWrapper {

    private final MediaPlayer.TrackInfo trackInfo;

    TrackInfoWrapper(MediaPlayer.TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    int type() {
        return trackInfo.getTrackType();
    }

    String language() {
        return trackInfo.getLanguage();
    }
}
