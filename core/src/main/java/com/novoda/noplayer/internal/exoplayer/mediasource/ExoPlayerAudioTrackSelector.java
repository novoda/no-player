package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.noplayer.internal.exoplayer.mediasource.TrackType.AUDIO;

public class ExoPlayerAudioTrackSelector {

    private final ExoPlayerTrackSelector trackSelector;

    public ExoPlayerAudioTrackSelector(ExoPlayerTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

    public boolean selectAudioTrack(PlayerAudioTrack audioTrack, RendererTypeRequester rendererTypeRequester) {
        TrackGroupArray trackGroups = trackSelector.trackGroups(AUDIO, rendererTypeRequester);

        DefaultTrackSelector.SelectionOverride selectionOverride = new DefaultTrackSelector.SelectionOverride(
                audioTrack.groupIndex(),
                audioTrack.formatIndex()
        );
        return trackSelector.setSelectionOverride(AUDIO, rendererTypeRequester, trackGroups, selectionOverride);
    }

    public AudioTracks getAudioTracks(RendererTypeRequester rendererTypeRequester) {
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
                            format.bitrate,
                            AudioTrackType.from(format.selectionFlags)
                    );
                    audioTracks.add(playerAudioTrack);
                }
            }
        }

        return AudioTracks.from(audioTracks);
    }

    public boolean clearAudioTrack(RendererTypeRequester rendererTypeRequester) {
        return trackSelector.clearSelectionOverrideFor(AUDIO, rendererTypeRequester);
    }
}
