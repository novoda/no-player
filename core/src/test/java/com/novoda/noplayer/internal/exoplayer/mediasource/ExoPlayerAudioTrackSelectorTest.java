package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ExoPlayerAudioTrackSelectorTest {

    private static final String ANY_TRACK_ID = "any_track_id";
    private static final String ANY_LANGUAGE = "any_language";
    private static final String ANY_MIME_TYPE = "any_mime_type";

    private static final int ANY_NUMBER_OF_CHANNELS = 2;
    private static final int ANY_FREQUENCY = 50;
    private static final int FIRST_GROUP = 0;
    private static final int FIRST_TRACK = 0;
    private static final int SECOND_GROUP = 1;
    private static final int THIRD_TRACK = 2;
    private static final int MAIN_AUDIO_TRACK_TYPE = 1;

    private static final Format AUDIO_FORMAT = AudioFormatFixture.anAudioFormat().withId("id1").withSelectionFlags(MAIN_AUDIO_TRACK_TYPE).build();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerTrackSelector trackSelector;
    @Mock
    private RendererTypeRequester rendererTypeRequester;

    private ExoPlayerAudioTrackSelector exoPlayerAudioTrackSelector;
    private static final PlayerAudioTrack AUDIO_TRACK = new PlayerAudioTrack(SECOND_GROUP, THIRD_TRACK, ANY_TRACK_ID, ANY_LANGUAGE, ANY_MIME_TYPE, ANY_NUMBER_OF_CHANNELS, ANY_FREQUENCY, AudioTrackType.MAIN);

    @Before
    public void setUp() {
        exoPlayerAudioTrackSelector = new ExoPlayerAudioTrackSelector(trackSelector);
    }

    @Test
    public void givenTrackSelectorContainsTracks_whenSelectingAudioTrack_thenSelectsTrack() {
        TrackGroupArray trackGroups = givenTrackSelectorContainsTracks();

        ArgumentCaptor<DefaultTrackSelector.SelectionOverride> argumentCaptor = whenSelectingAudioTrack(trackGroups);

        DefaultTrackSelector.SelectionOverride selectionOverride = argumentCaptor.getValue();
        assertThat(selectionOverride.groupIndex).isEqualTo(SECOND_GROUP);
        assertThat(selectionOverride.tracks).contains(THIRD_TRACK);
    }

    @Test
    public void givenTrackSelectorContainsUnsupportedTracks_whenGettingAudioTracks_thenReturnsOnlySupportedTracks() {
        givenTrackSelectorContainsUnsupportedTracks();

        AudioTracks actualAudioTracks = exoPlayerAudioTrackSelector.getAudioTracks(rendererTypeRequester);

        assertThat(actualAudioTracks).isEqualTo(expectedSupportedAudioTracks());
    }

    private TrackGroupArray givenTrackSelectorContainsTracks() {
        TrackGroupArray trackGroups = new TrackGroupArray(
                new TrackGroup(AudioFormatFixture.anAudioFormat().build()),
                new TrackGroup(
                        AudioFormatFixture.anAudioFormat().build(),
                        AudioFormatFixture.anAudioFormat().build(),
                        AudioFormatFixture.anAudioFormat().build()
                )
        );
        given(trackSelector.trackGroups(TrackType.AUDIO, rendererTypeRequester)).willReturn(trackGroups);

        return trackGroups;
    }

    private ArgumentCaptor<DefaultTrackSelector.SelectionOverride> whenSelectingAudioTrack(TrackGroupArray trackGroups) {
        exoPlayerAudioTrackSelector.selectAudioTrack(AUDIO_TRACK, rendererTypeRequester);

        ArgumentCaptor<DefaultTrackSelector.SelectionOverride> argumentCaptor = ArgumentCaptor.forClass(DefaultTrackSelector.SelectionOverride.class);
        verify(trackSelector).setSelectionOverride(eq(TrackType.AUDIO), any(RendererTypeRequester.class), eq(trackGroups), argumentCaptor.capture());
        return argumentCaptor;
    }

    private void givenTrackSelectorContainsUnsupportedTracks() {
        TrackGroupArray trackGroups = new TrackGroupArray(
                new TrackGroup(AUDIO_FORMAT),
                new TrackGroup(
                        AudioFormatFixture.anAudioFormat().build(),
                        AudioFormatFixture.anAudioFormat().build(),
                        AudioFormatFixture.anAudioFormat().build()
                )
        );
        given(trackSelector.trackGroups(TrackType.AUDIO, rendererTypeRequester)).willReturn(trackGroups);
        given(trackSelector.supportsTrackSwitching(eq(TrackType.AUDIO), any(RendererTypeRequester.class), any(TrackGroupArray.class), anyInt()))
                .willReturn(true)
                .willReturn(false);
    }

    private AudioTracks expectedSupportedAudioTracks() {
        return AudioTracks.from(
                Collections.singletonList(
                        new PlayerAudioTrack(
                                FIRST_GROUP,
                                FIRST_TRACK,
                                AUDIO_FORMAT.id,
                                AUDIO_FORMAT.language,
                                AUDIO_FORMAT.sampleMimeType,
                                AUDIO_FORMAT.channelCount,
                                AUDIO_FORMAT.bitrate,
                                AudioTrackType.from(AUDIO_FORMAT.selectionFlags)

                        )
                )
        );
    }
}
