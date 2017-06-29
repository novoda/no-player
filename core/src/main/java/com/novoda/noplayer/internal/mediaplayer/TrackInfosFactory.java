package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

class TrackInfosFactory {

    NoPlayerTrackInfos createFrom(MediaPlayer mediaPlayer) {
        MediaPlayer.TrackInfo[] mediaPlayerTrackInfos = mediaPlayer.getTrackInfo();

        List<NoPlayerTrackInfo> trackInfos = new ArrayList<>(mediaPlayerTrackInfos.length);
        for (MediaPlayer.TrackInfo mediaPlayerTrackInfo : mediaPlayerTrackInfos) {
            trackInfos.add(new NoPlayerTrackInfo(mediaPlayerTrackInfo));
        }

        return new NoPlayerTrackInfos(trackInfos);
    }
}
