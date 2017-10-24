package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.internal.exoplayer.mediasource.TrackType.AUDIO;
import static com.novoda.noplayer.internal.exoplayer.mediasource.TrackType.VIDEO;

public class ExoPlayerVideoTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;

    public ExoPlayerVideoTrackSelector(ExoPlayerTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    public List<PlayerVideoTrack> getVideoTracks(RendererTypeRequester rendererTypeRequester, ContentType contentType) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(VIDEO, rendererTypeRequester);

        List<PlayerVideoTrack> videoTracks = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            if (trackSelector.supportsTrackSwitching(AUDIO, rendererTypeRequester, trackGroups, groupIndex)) {
                TrackGroup trackGroup = trackGroups.get(groupIndex);

                for (int formatIndex = 0; formatIndex < trackGroup.length; formatIndex++) {
                    Format format = trackGroup.getFormat(formatIndex);

                    PlayerVideoTrack playerVideoTrack = new PlayerVideoTrack(
                            contentType,
                            format.width,
                            format.height,
                            (int) format.frameRate,
                            format.bitrate
                    );

                    videoTracks.add(playerVideoTrack);
                }
            }
        }

        return videoTracks;
    }
}
