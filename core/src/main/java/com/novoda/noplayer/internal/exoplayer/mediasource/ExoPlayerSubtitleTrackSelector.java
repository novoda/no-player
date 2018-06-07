package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.PlayerSubtitleTrack;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.internal.exoplayer.mediasource.TrackType.TEXT;

public class ExoPlayerSubtitleTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;

    public ExoPlayerSubtitleTrackSelector(ExoPlayerTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    public boolean selectTextTrack(PlayerSubtitleTrack subtitleTrack, RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(TEXT, rendererTypeRequester);

        DefaultTrackSelector.SelectionOverride selectionOverride = new DefaultTrackSelector.SelectionOverride(
                subtitleTrack.groupIndex(),
                subtitleTrack.formatIndex()
        );
        return trackSelector.setSelectionOverride(TEXT, rendererTypeRequester, trackGroups, selectionOverride);
    }

    public List<PlayerSubtitleTrack> getSubtitleTracks(RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(TEXT, rendererTypeRequester);

        List<PlayerSubtitleTrack> subtitleTracks = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup trackGroup = trackGroups.get(groupIndex);

            for (int formatIndex = 0; formatIndex < trackGroup.length; formatIndex++) {
                Format format = trackGroup.getFormat(formatIndex);
                PlayerSubtitleTrack playerSubtitleTrack = new PlayerSubtitleTrack(
                        groupIndex,
                        formatIndex,
                        format.id,
                        format.language,
                        format.sampleMimeType,
                        format.channelCount,
                        format.bitrate
                );
                subtitleTracks.add(playerSubtitleTrack);
            }
        }

        return subtitleTracks;
    }

    public boolean clearSubtitleTrack(RendererTypeRequester rendererTypeRequester) {
        return trackSelector.clearSelectionOverrideFor(TEXT, rendererTypeRequester);
    }
}
