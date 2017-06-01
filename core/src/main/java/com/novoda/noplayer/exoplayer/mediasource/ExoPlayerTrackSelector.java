package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;

import static com.google.android.exoplayer2.C.TRACK_TYPE_AUDIO;

public class ExoPlayerTrackSelector {

    private final DefaultTrackSelector trackSelector;

    public ExoPlayerTrackSelector(DefaultTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    TrackGroupArray getAudioTrackGroups() {
        MappingTrackSelector.MappedTrackInfo trackInfo = getTrackInfo();
        return trackInfo.getTrackGroups(TRACK_TYPE_AUDIO);
    }

    private MappingTrackSelector.MappedTrackInfo getTrackInfo() {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();

        if (trackInfo == null) {
            throw new NullPointerException("Track info is not available.");
        }
        return trackInfo;
    }

    void setSelectionOverride(TrackGroupArray trackGroups, MappingTrackSelector.SelectionOverride selectionOverride) {
        trackSelector.setSelectionOverride(TRACK_TYPE_AUDIO, trackGroups, selectionOverride);
    }

    boolean supportsTrackSwitching(TrackGroupArray trackGroups, int groupIndex) {
        MappingTrackSelector.MappedTrackInfo trackInfo = getTrackInfo();

        return trackGroups.get(groupIndex).length > 0
                && trackInfo.getAdaptiveSupport(TRACK_TYPE_AUDIO, groupIndex, false) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }

}
