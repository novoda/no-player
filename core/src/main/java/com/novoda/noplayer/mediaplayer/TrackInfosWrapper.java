package com.novoda.noplayer.mediaplayer;

import java.util.List;

class TrackInfosWrapper {

    private final List<TrackInfoWrapper> trackInfos;

    TrackInfosWrapper(List<TrackInfoWrapper> trackInfos) {
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
