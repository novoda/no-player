package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.utils.Optional;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.internal.exoplayer.mediasource.TrackType.VIDEO;

public class ExoPlayerVideoTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;
    private final TrackSelection.Factory trackSelectionFactory;

    public ExoPlayerVideoTrackSelector(ExoPlayerTrackSelector trackSelector, TrackSelection.Factory trackSelectionFactory) {
        this.trackSelector = trackSelector;
        this.trackSelectionFactory = trackSelectionFactory;
    }

    public boolean selectVideoTrack(PlayerVideoTrack videoTrack, RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(VIDEO, rendererTypeRequester);

        MappingTrackSelector.SelectionOverride selectionOverride = new MappingTrackSelector.SelectionOverride(
                trackSelectionFactory,
                videoTrack.groupIndex(),
                videoTrack.formatIndex()
        );
        return trackSelector.setSelectionOverride(VIDEO, rendererTypeRequester, trackGroups, selectionOverride);
    }

    public List<PlayerVideoTrack> getVideoTracks(RendererTypeRequester rendererTypeRequester, ContentType contentType) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(VIDEO, rendererTypeRequester);

        List<PlayerVideoTrack> videoTracks = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup trackGroup = trackGroups.get(groupIndex);

            for (int formatIndex = 0; formatIndex < trackGroup.length; formatIndex++) {
                Format format = trackGroup.getFormat(formatIndex);

                PlayerVideoTrack playerVideoTrack = new PlayerVideoTrack(
                        groupIndex,
                        formatIndex,
                        format.id,
                        contentType,
                        format.width,
                        format.height,
                        (int) format.frameRate,
                        format.bitrate
                );

                videoTracks.add(playerVideoTrack);
            }
        }

        return videoTracks;
    }

    public Optional<PlayerVideoTrack> getSelectedVideoTrack(SimpleExoPlayer exoPlayer, RendererTypeRequester rendererTypeRequester, ContentType contentType) {
        Format selectedVideoFormat = exoPlayer.getVideoFormat();

        if (selectedVideoFormat == null) {
            return Optional.absent();
        }

        List<PlayerVideoTrack> videoTracks = getVideoTracks(rendererTypeRequester, contentType);
        return findSelectedVideoTrack(selectedVideoFormat, videoTracks);
    }

    private Optional<PlayerVideoTrack> findSelectedVideoTrack(Format selectedVideoFormat, List<PlayerVideoTrack> videoTracks) {
        for (PlayerVideoTrack videoTrack : videoTracks) {
            if (videoTrack.id().equals(selectedVideoFormat.id)) {
                return Optional.of(videoTrack);
            }
        }
        return Optional.absent();
    }

    public boolean clearVideoTrack(RendererTypeRequester rendererTypeRequester) {
        return trackSelector.clearSelectionOverrideFor(VIDEO, rendererTypeRequester);
    }
}
