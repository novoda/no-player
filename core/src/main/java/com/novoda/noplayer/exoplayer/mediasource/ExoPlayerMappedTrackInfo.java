package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;

public class ExoPlayerMappedTrackInfo {

    private final MappingTrackSelector.MappedTrackInfo mappedTrackInfo;

    public ExoPlayerMappedTrackInfo(MappingTrackSelector.MappedTrackInfo mappedTrackInfo) {
        this.mappedTrackInfo = mappedTrackInfo;
    }

    public int length() {
        return mappedTrackInfo.length;
    }

    public TrackGroupArray getTrackGroups(int index) {
        return mappedTrackInfo.getTrackGroups(index);
    }

    public int getAdaptiveSupport(Integer rendererIndex, int groupIndex, boolean includeCapabilitiesExceededTracks) {
        return mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, includeCapabilitiesExceededTracks);
    }
}
