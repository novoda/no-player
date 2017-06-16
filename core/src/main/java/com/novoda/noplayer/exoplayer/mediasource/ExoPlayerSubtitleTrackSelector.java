package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.novoda.noplayer.PlayerSubtitleTrack;
import com.novoda.utils.NoPlayerLog;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.exoplayer.mediasource.TrackType.TEXT;

public class ExoPlayerSubtitleTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;
    private final TrackSelection.Factory trackSelectionFactory;

    public ExoPlayerSubtitleTrackSelector(ExoPlayerTrackSelector trackSelector, TrackSelection.Factory trackSelectionFactory) {
        this.trackSelector = trackSelector;
        this.trackSelectionFactory = trackSelectionFactory;
    }

    public void selectTextTrack(PlayerSubtitleTrack subtitleTrack) {
        TrackGroupArray trackGroups = trackSelector.getSubtitleTrackGroups();

        MappingTrackSelector.SelectionOverride selectionOverride = new MappingTrackSelector.SelectionOverride(
                trackSelectionFactory,
                subtitleTrack.groupIndex(),
                subtitleTrack.formatIndex()
        );
        trackSelector.setSelectionOverride(TEXT, trackGroups, selectionOverride);
    }

    public List<PlayerSubtitleTrack> getSubtitleTracks() {
        TrackGroupArray trackGroups = trackSelector.getSubtitleTrackGroups();

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

    public void clearSubtitleTrack() {
        trackSelector.clearSelectionOverrideFor(TEXT);
    }

    public void selectFirstTextTrack() {
        List<PlayerSubtitleTrack> subtitleTracks = getSubtitleTracks();
        if (subtitleTracks.isEmpty()) {
            NoPlayerLog.e("No subtitles tracks available");
        } else {
            selectTextTrack(subtitleTracks.get(0));
        }
    }
}
