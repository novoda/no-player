package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.exoplayer.mediasource.TrackType.AUDIO;

public class ExoPlayerAudioTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;
    private final TrackSelection.Factory trackSelectionFactory;

    public ExoPlayerAudioTrackSelector(ExoPlayerTrackSelector trackSelector, TrackSelection.Factory trackSelectionFactory) {
        this.trackSelector = trackSelector;
        this.trackSelectionFactory = trackSelectionFactory;
    }

    public void selectAudioTrack(PlayerAudioTrack audioTrack, RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(AUDIO, rendererTypeRequester);

        MappingTrackSelector.SelectionOverride selectionOverride = new MappingTrackSelector.SelectionOverride(
                trackSelectionFactory,
                audioTrack.groupIndex(),
                audioTrack.formatIndex()
        );
        trackSelector.setSelectionOverride(AUDIO, rendererTypeRequester, trackGroups, selectionOverride);
    }

    public List<PlayerAudioTrack> getAudioTracks(RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(AUDIO, rendererTypeRequester);

        List<PlayerAudioTrack> audioTracks = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            if (trackSelector.supportsTrackSwitching(AUDIO, rendererTypeRequester, trackGroups, groupIndex)) {
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
}
