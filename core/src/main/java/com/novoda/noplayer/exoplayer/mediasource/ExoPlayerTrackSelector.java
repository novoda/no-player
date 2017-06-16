package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.novoda.noplayer.exoplayer.TrackType;

import java.util.EnumMap;
import java.util.Map;

import static com.novoda.noplayer.exoplayer.TrackType.AUDIO;
import static com.novoda.noplayer.exoplayer.TrackType.TEXT;

public class ExoPlayerTrackSelector {

    private final DefaultTrackSelector trackSelector;

    private EnumMap<TrackType, Integer> rendererTrackIndex;

    public ExoPlayerTrackSelector(DefaultTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    TrackGroupArray getAudioTrackGroups() {
        Integer audioRendererIndex = rendererTrackIndex.get(AUDIO);
        return trackInfo().getTrackGroups(audioRendererIndex);
    }

    TrackGroupArray getSubtitleTrackGroups() {
        Integer subtitleRendererIndex = rendererTrackIndex.get(TEXT);
        return trackInfo().getTrackGroups(subtitleRendererIndex);
    }

    public void clearSelectionOverrideFor(TrackType trackType) {
        Integer rendererIndex = rendererTrackIndex.get(trackType);
        trackSelector.clearSelectionOverrides(rendererIndex);
    }

    public ExoPlayerMappedTrackInfo trackInfo() {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();

        if (trackInfo == null) {
            throw new NullPointerException("Track info is not available.");
        }
        return new ExoPlayerMappedTrackInfo(trackInfo);
    }

    public static class ExoPlayerMappedTrackInfo {

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

    void setSelectionOverride(TrackType trackType, TrackGroupArray trackGroups, MappingTrackSelector.SelectionOverride selectionOverride) {
        Integer rendererIndex = rendererTrackIndex.get(trackType);
        trackSelector.setSelectionOverride(rendererIndex, trackGroups, selectionOverride);
    }

    boolean supportsTrackSwitching(TrackType trackType, TrackGroupArray trackGroups, int groupIndex) {
        Integer audioRendererIndex = rendererTrackIndex.get(trackType);
        return trackGroups.get(groupIndex).length > 0
                && trackInfo().getAdaptiveSupport(audioRendererIndex, groupIndex, false) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }

    public void setTrackRendererIndexes(Map<TrackType, Integer> trackTypeIndexMap) {
        rendererTrackIndex = new EnumMap<>(trackTypeIndexMap);
    }
}
