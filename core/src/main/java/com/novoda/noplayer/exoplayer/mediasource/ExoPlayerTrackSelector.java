package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;
import com.novoda.utils.Optional;

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

    TrackGroupArray trackGroups(TrackType trackType, RendererTypeRequester rendererTypeRequester) {
        Optional<Integer> audioRendererIndex = rendererTrackIndexExtractor.extract(trackType, mappedTrackInfoLength(), rendererTypeRequester);
        return audioRendererIndex.isAbsent() ? TrackGroupArray.EMPTY : trackInfo().getTrackGroups(audioRendererIndex.get());
    }

    public void clearSelectionOverrideFor(TrackType trackType, RendererTypeRequester rendererTypeRequester) {
        Optional<Integer> audioRendererIndex = rendererTrackIndexExtractor.extract(trackType, mappedTrackInfoLength(), rendererTypeRequester);
        if (audioRendererIndex.isPresent()) {
            trackSelector.clearSelectionOverrides(audioRendererIndex.get());
        }
    }

    public ExoPlayerMappedTrackInfo trackInfo() {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();

        if (trackInfo == null) {
            throw new NullPointerException("Track info is not available.");
        }
        return new ExoPlayerMappedTrackInfo(trackInfo);
    }

    private int mappedTrackInfoLength() {
        return trackSelector.getCurrentMappedTrackInfo().length;
    }

    boolean setSelectionOverride(TrackType trackType,
                                 RendererTypeRequester rendererTypeRequester,
                                 TrackGroupArray trackGroups,
                                 MappingTrackSelector.SelectionOverride selectionOverride) {
        Optional<Integer> audioRendererIndex = rendererTrackIndexExtractor.extract(trackType, mappedTrackInfoLength(), rendererTypeRequester);
        if (audioRendererIndex.isPresent()) {
            trackSelector.setSelectionOverride(audioRendererIndex.get(), trackGroups, selectionOverride);
            return true;
        } else {
            return false;
        }
    }

    boolean supportsTrackSwitching(TrackType trackType,
                                   RendererTypeRequester rendererTypeRequester,
                                   TrackGroupArray trackGroups,
                                   int groupIndex) {
        Optional<Integer> audioRendererIndex = rendererTrackIndexExtractor.extract(trackType, mappedTrackInfoLength(), rendererTypeRequester);
        return audioRendererIndex.isPresent()
                && trackGroups.get(groupIndex).length > 0
                && trackInfo().getAdaptiveSupport(audioRendererIndex.get(), groupIndex, false) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }
}
