package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.novoda.noplayer.PlayerAudioTrack;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.exoplayer2.C.TRACK_TYPE_AUDIO;

public class ExoPlayerTrackSelector {

    private final DefaultTrackSelector trackSelector;

    public ExoPlayerTrackSelector(DefaultTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    public void selectAudioTrack(PlayerAudioTrack audioTrack) {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();
        TrackGroupArray trackGroups = trackInfo.getTrackGroups(TRACK_TYPE_AUDIO);
        FixedTrackSelection.Factory factory = new FixedTrackSelection.Factory();

        MappingTrackSelector.SelectionOverride selectionOverride = new MappingTrackSelector.SelectionOverride(
                factory,
                audioTrack.groupIndex(),
                audioTrack.formatIndex()
        );
        trackSelector.setSelectionOverride(TRACK_TYPE_AUDIO, trackGroups, selectionOverride);
    }

    public List<PlayerAudioTrack> getAudioTracks() {
        MappingTrackSelector.MappedTrackInfo trackInfo = trackSelector.getCurrentMappedTrackInfo();

        if (trackInfo == null) {
            throw new NullPointerException("Track info is not available.");
        }

        TrackGroupArray trackGroups = trackInfo.getTrackGroups(TRACK_TYPE_AUDIO);

        List<PlayerAudioTrack> audioTracks = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            if (trackSwitchingSupported(trackGroups, trackInfo, groupIndex)) {
                TrackGroup trackGroup = trackGroups.get(groupIndex);

                for (int formatIndex = 0; formatIndex < trackGroup.length; formatIndex++) {
                    Format format = trackGroup.getFormat(formatIndex);
                    PlayerAudioTrack playerAudioTrack = new PlayerAudioTrack(
                            groupIndex,
                            formatIndex,
                            format.id,
                            format.language,
                            format.sampleMimeType,
                            format.channelCount,
                            format.bitrate
                    );
                    audioTracks.add(playerAudioTrack);
                }
            }
        }

        return audioTracks;
    }

    private boolean trackSwitchingSupported(TrackGroupArray trackGroups, MappingTrackSelector.MappedTrackInfo trackInfo, int groupIndex) {
        return trackGroups.get(groupIndex).length > 0
                && trackInfo.getAdaptiveSupport(TRACK_TYPE_AUDIO, groupIndex, false) != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }
}
