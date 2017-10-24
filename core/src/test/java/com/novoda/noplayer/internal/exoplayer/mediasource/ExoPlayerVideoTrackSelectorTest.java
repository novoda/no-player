package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.novoda.noplayer.internal.exoplayer.mediasource.VideoFormatFixture.aVideoFormat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ExoPlayerVideoTrackSelectorTest {

    private static final String ANY_TRACK_ID = "id1";
    private static final Format VIDEO_FORMAT = aVideoFormat().withId(ANY_TRACK_ID).build();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerTrackSelector trackSelector;
    @Mock
    private TrackSelection.Factory trackSelectionFactory;
    @Mock
    private RendererTypeRequester rendererTypeRequester;

    private ExoPlayerVideoTrackSelector exoPlayerVideoTrackSelector;

    @Before
    public void setUp() {
        exoPlayerVideoTrackSelector = new ExoPlayerVideoTrackSelector(trackSelector);
    }

    @Test
    public void givenTrackSelectorContainsUnsupportedTracks_whenGettingVideoTracks_thenReturnsSupportedTracks() {
        givenTrackSelectorContainsTracks();

        List<PlayerVideoTrack> actualVideoTracks = exoPlayerVideoTrackSelector.getVideoTracks(rendererTypeRequester, ContentType.HLS);

        assertThat(actualVideoTracks).isEqualTo(expectedSupportedVideoTracks());
    }

    private void givenTrackSelectorContainsTracks() {
        TrackGroupArray trackGroups = new TrackGroupArray(
                new TrackGroup(VIDEO_FORMAT)
        );
        given(trackSelector.trackGroups(TrackType.VIDEO, rendererTypeRequester)).willReturn(trackGroups);
    }

    private List<PlayerVideoTrack> expectedSupportedVideoTracks() {
        return Collections.singletonList(
                new PlayerVideoTrack(
                        VIDEO_FORMAT.id,
                        ContentType.HLS,
                        VIDEO_FORMAT.width,
                        VIDEO_FORMAT.height,
                        (int) VIDEO_FORMAT.frameRate,
                        VIDEO_FORMAT.bitrate
                )
        );
    }
}
