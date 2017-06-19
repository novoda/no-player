package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;

public class ExoPlayerTrackSelector {

    private final DefaultTrackSelector trackSelector;
    private final RendererTrackIndexExtractor rendererTrackIndexExtractor;

    public static ExoPlayerTrackSelector newInstance(DefaultTrackSelector trackSelector) {
        RendererTrackIndexExtractor rendererTrackIndexExtractor = new RendererTrackIndexExtractor();
        return new ExoPlayerTrackSelector(trackSelector, rendererTrackIndexExtractor);
    }

    ExoPlayerTrackSelector(DefaultTrackSelector trackSelector, RendererTrackIndexExtractor rendererTrackIndexExtractor) {
        this.trackSelector = trackSelector;
        this.rendererTrackIndexExtractor = rendererTrackIndexExtractor;
    }

    TrackGroupArray getTrackGroups(TrackType trackType, RendererTypeRequester rendererTypeRequester) {
        Integer audioRendererIndex = rendererTrackIndexExtractor.get(trackType, getMappedTrackInfoLength(), rendererTypeRequester);
        return trackInfo().getTrackGroups(audioRendererIndex);
    }

    public void clearSelectionOverrideFor(TrackType trackType, RendererTypeRequester rendererTypeRequester) {
        Integer rendererIndex = rendererTrackIndexExtractor.get(trackType, getMappedTrackInfoLength(), rendererTypeRequester);
        trackSelector.clearSelectionOverrides(rendererIndex);
    }

    public ExoPlayerMappedTrackInfo trackInfo() {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();

        if (trackInfo == null) {
            throw new NullPointerException("Track info is not available.");
        }
        return new ExoPlayerMappedTrackInfo(trackInfo);
    }

    private int getMappedTrackInfoLength() {
        return trackSelector.getCurrentMappedTrackInfo().length;
    }

    void setSelectionOverride(TrackType trackType,
                              RendererTypeRequester rendererTypeRequester,
                              TrackGroupArray trackGroups,
                              MappingTrackSelector.SelectionOverride selectionOverride) {
        int rendererIndex = rendererTrackIndexExtractor.get(trackType, getMappedTrackInfoLength(), rendererTypeRequester);
        trackSelector.setSelectionOverride(rendererIndex, trackGroups, selectionOverride);
    }

    boolean supportsTrackSwitching(TrackType trackType,
                                   RendererTypeRequester rendererTypeRequester,
                                   TrackGroupArray trackGroups,
                                   int groupIndex) {
        int audioRendererIndex = rendererTrackIndexExtractor.get(trackType, getMappedTrackInfoLength(), rendererTypeRequester);
        return trackGroups.get(groupIndex).length > 0
                && trackInfo().getAdaptiveSupport(audioRendererIndex, groupIndex, false) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }
}
