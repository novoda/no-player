package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerMappedTrackInfo;

import java.util.EnumMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class RendererTrackIndexExtractorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerMappedTrackInfo trackCounter;
    @Mock
    private SimpleExoPlayer simpleExoPlayer;

    private RendererTrackIndexExtractor extractor;

    @Before
    public void setUp() throws Exception {
        extractor = new RendererTrackIndexExtractor();
        given(trackCounter.length()).willReturn(1);
    }

    @Test
    public void givenAudioTrackAtPositionZero_whenExtractingTrackIndexes_thenReturnsCorrectMap() {
        given(simpleExoPlayer.getRendererType(0)).willReturn(C.TRACK_TYPE_AUDIO);
        Map<TrackType, Integer> expectedMap = new EnumMap<>(TrackType.class);
        expectedMap.put(TrackType.AUDIO, 0);

        Map<TrackType, Integer> trackTypeIntegerMap = extractor.extractFrom(trackCounter, simpleExoPlayer);

        assertThat(trackTypeIntegerMap).isEqualTo(expectedMap);
    }

    @Test
    public void givenVideoTrackAtPositionZero_whenExtractingTrackIndexes_thenReturnsCorrectMap() {
        given(simpleExoPlayer.getRendererType(0)).willReturn(C.TRACK_TYPE_VIDEO);
        Map<TrackType, Integer> expectedMap = new EnumMap<>(TrackType.class);
        expectedMap.put(TrackType.VIDEO, 0);

        Map<TrackType, Integer> trackTypeIntegerMap = extractor.extractFrom(trackCounter, simpleExoPlayer);

        assertThat(trackTypeIntegerMap).isEqualTo(expectedMap);
    }

    @Test
    public void givenSubtitlesTrackAtPositionZero_whenExtractingTrackIndexes_thenReturnsCorrectMap() {
        given(simpleExoPlayer.getRendererType(0)).willReturn(C.TRACK_TYPE_TEXT);
        Map<TrackType, Integer> expectedMap = new EnumMap<>(TrackType.class);
        expectedMap.put(TrackType.TEXT, 0);

        Map<TrackType, Integer> trackTypeIntegerMap = extractor.extractFrom(trackCounter, simpleExoPlayer);

        assertThat(trackTypeIntegerMap).isEqualTo(expectedMap);
    }
}
