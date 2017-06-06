package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

class TrackInfosWrapper {

    private final List<TrackInfoWrapper> trackInfos;

    static TrackInfosWrapper from(MediaPlayer.TrackInfo[] mediaPlayerTrackInfos) {
        List<TrackInfoWrapper> trackInfos = new ArrayList<>(mediaPlayerTrackInfos.length);
        for (MediaPlayer.TrackInfo mediaPlayerTrackInfo : mediaPlayerTrackInfos) {
            trackInfos.add(new TrackInfoWrapper(mediaPlayerTrackInfo));
        }
        return new TrackInfosWrapper(trackInfos);
    }

    private TrackInfosWrapper(List<TrackInfoWrapper> trackInfos) {
        this.trackInfos = trackInfos;
    }

    TrackInfoWrapper get(int index) {
        return trackInfos.get(index);
    }

    int size() {
        return trackInfos.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TrackInfosWrapper that = (TrackInfosWrapper) o;

        return trackInfos != null ? trackInfos.equals(that.trackInfos) : that.trackInfos == null;
    }

    @Override
    public int hashCode() {
        return trackInfos != null ? trackInfos.hashCode() : 0;
    }
}
