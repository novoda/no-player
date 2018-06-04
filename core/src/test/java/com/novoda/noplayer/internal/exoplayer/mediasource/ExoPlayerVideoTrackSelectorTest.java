package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.novoda.noplayer.internal.exoplayer.mediasource.VideoFormatFixture.aVideoFormat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ExoPlayerVideoTrackSelectorTest {

    private static final Format VIDEO_FORMAT = aVideoFormat().withId("id1").build();
    private static final PlayerVideoTrack PLAYER_VIDEO_TRACK = new PlayerVideoTrack(
            0,
            0,
            VIDEO_FORMAT.id,
            ContentType.HLS,
            VIDEO_FORMAT.width,
            VIDEO_FORMAT.height,
            (int) VIDEO_FORMAT.frameRate,
            VIDEO_FORMAT.bitrate
    );

    private static final Format ADDITIONAL_VIDEO_FORMAT = aVideoFormat().withId("id2").build();
    private static final int FIRST_GROUP = 0;
    private static final int SECOND_TRACK = 1;
    private static final PlayerVideoTrack ADDITIONAL_PLAYER_VIDEO_TRACK = new PlayerVideoTrack(
            FIRST_GROUP,
            SECOND_TRACK,
            ADDITIONAL_VIDEO_FORMAT.id,
            ContentType.HLS,
            ADDITIONAL_VIDEO_FORMAT.width,
            ADDITIONAL_VIDEO_FORMAT.height,
            (int) ADDITIONAL_VIDEO_FORMAT.frameRate,
            ADDITIONAL_VIDEO_FORMAT.bitrate
    );

    private static final List<PlayerVideoTrack> EXPECTED_TRACKS = Arrays.asList(PLAYER_VIDEO_TRACK, ADDITIONAL_PLAYER_VIDEO_TRACK);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerTrackSelector trackSelector;
    @Mock
    private TrackSelection.Factory trackSelectionFactory;
    @Mock
    private RendererTypeRequester rendererTypeRequester;
    @Mock
    private SimpleExoPlayer exoPlayer;

    private ExoPlayerVideoTrackSelector exoPlayerVideoTrackSelector;

    @Before
    public void setUp() {
        exoPlayerVideoTrackSelector = new ExoPlayerVideoTrackSelector(trackSelector);
    }

    @Test
    public void givenTrackSelectorContainsTracks_whenSelectingVideoTrack_thenSelectsTrack() {
        givenTrackSelectorContainsTracks();

        ArgumentCaptor<DefaultTrackSelector.SelectionOverride> argumentCaptor = whenSelectingVideoTrack(ADDITIONAL_PLAYER_VIDEO_TRACK);

        DefaultTrackSelector.SelectionOverride selectionOverride = argumentCaptor.getValue();
        assertThat(selectionOverride.groupIndex).isEqualTo(FIRST_GROUP);
        assertThat(selectionOverride.tracks).contains(SECOND_TRACK);
    }

    @Test
    public void givenTrackSelector_whenGettingVideoTracks_thenReturnsSupportedTracks() {
        givenTrackSelectorContainsTracks();

        List<PlayerVideoTrack> actualVideoTracks = exoPlayerVideoTrackSelector.getVideoTracks(rendererTypeRequester, ContentType.HLS);

        assertThat(actualVideoTracks).isEqualTo(EXPECTED_TRACKS);
    }

    @Test
    public void givenTrackSelector_whenGettingCurrentlySelectedVideoTrack_thenReturnsSelectedTrack() {
        givenTrackSelectorContainsTracks();
        given(exoPlayer.getVideoFormat()).willReturn(ADDITIONAL_VIDEO_FORMAT);

        Optional<PlayerVideoTrack> selectedVideoTrack = exoPlayerVideoTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, ContentType.HLS);

        assertThat(selectedVideoTrack).isEqualTo(Optional.of(ADDITIONAL_PLAYER_VIDEO_TRACK));
    }

    @Test
    public void givenNoCurrentlySelectedTrack_whenGettingCurrentlySelectedVideoTrack_thenReturnsAbsent() {
        givenTrackSelectorContainsTracks();
        given(exoPlayer.getVideoFormat()).willReturn(null);

        Optional<PlayerVideoTrack> selectedVideoTrack = exoPlayerVideoTrackSelector.getSelectedVideoTrack(exoPlayer, rendererTypeRequester, ContentType.HLS);

        assertThat(selectedVideoTrack).isEqualTo(Optional.<PlayerVideoTrack>absent());
    }

    private void givenTrackSelectorContainsTracks() {
        TrackGroupArray trackGroups = new TrackGroupArray(
                new TrackGroup(VIDEO_FORMAT, ADDITIONAL_VIDEO_FORMAT)
        );
        given(trackSelector.trackGroups(TrackType.VIDEO, rendererTypeRequester)).willReturn(trackGroups);
    }

    private ArgumentCaptor<DefaultTrackSelector.SelectionOverride> whenSelectingVideoTrack(PlayerVideoTrack videoTrack) {
        exoPlayerVideoTrackSelector.selectVideoTrack(videoTrack, rendererTypeRequester);

        ArgumentCaptor<DefaultTrackSelector.SelectionOverride> argumentCaptor = ArgumentCaptor.forClass(DefaultTrackSelector.SelectionOverride.class);
        verify(trackSelector).setSelectionOverride(eq(TrackType.VIDEO), any(RendererTypeRequester.class), any(TrackGroupArray.class), argumentCaptor.capture());
        return argumentCaptor;
    }
}
