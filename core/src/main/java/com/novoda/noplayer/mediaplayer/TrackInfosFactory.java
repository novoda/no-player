package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

class TrackInfosFactory {

    TrackInfosWrapper createFrom(MediaPlayer mediaPlayer) {
        MediaPlayer.TrackInfo[] mediaPlayerTrackInfos = mediaPlayer.getTrackInfo();

        List<TrackInfoWrapper> trackInfos = new ArrayList<>(mediaPlayerTrackInfos.length);
        for (MediaPlayer.TrackInfo mediaPlayerTrackInfo : mediaPlayerTrackInfos) {
            trackInfos.add(new TrackInfoWrapper(mediaPlayerTrackInfo));
        }

        return new TrackInfosWrapper(trackInfos);
    }
}
