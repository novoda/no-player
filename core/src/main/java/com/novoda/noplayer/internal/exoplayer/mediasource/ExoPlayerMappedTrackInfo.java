package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;

class ExoPlayerMappedTrackInfo {

    private final MappingTrackSelector.MappedTrackInfo mappedTrackInfo;

    ExoPlayerMappedTrackInfo(MappingTrackSelector.MappedTrackInfo mappedTrackInfo) {
        this.mappedTrackInfo = mappedTrackInfo;
    }

    TrackGroupArray getTrackGroups(int index) {
        return mappedTrackInfo.getTrackGroups(index);
    }

    int getAdaptiveSupport(int rendererIndex, int groupIndex, boolean includeCapabilitiesExceededTracks) {
        return mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, includeCapabilitiesExceededTracks);
    }
}
