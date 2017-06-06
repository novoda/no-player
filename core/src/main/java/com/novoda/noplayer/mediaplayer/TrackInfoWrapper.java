package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

class TrackInfoWrapper {

    private final MediaPlayer.TrackInfo trackInfo;

    public TrackInfoWrapper(MediaPlayer.TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public int type() {
        return trackInfo.getTrackType();
    }

    public String language() {
        return trackInfo.getLanguage();
    }
}
