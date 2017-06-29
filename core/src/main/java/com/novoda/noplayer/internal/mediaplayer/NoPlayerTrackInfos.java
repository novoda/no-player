package com.novoda.noplayer.internal.mediaplayer;

import java.util.List;

class NoPlayerTrackInfos {

    private final List<NoPlayerTrackInfo> trackInfos;

    NoPlayerTrackInfos(List<NoPlayerTrackInfo> trackInfos) {
        this.trackInfos = trackInfos;
    }

    NoPlayerTrackInfo get(int index) {
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

        NoPlayerTrackInfos that = (NoPlayerTrackInfos) o;

        return trackInfos != null ? trackInfos.equals(that.trackInfos) : that.trackInfos == null;
    }

    @Override
    public int hashCode() {
        return trackInfos != null ? trackInfos.hashCode() : 0;
    }
}
