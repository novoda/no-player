package com.novoda.noplayer.exoplayer.mediasource;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.exoplayer.RendererTypeRequester;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;

public class RendererTrackIndexExtractorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private RendererTrackIndexExtractor extractor;

    @Before
    public void setUp() throws Exception {
        extractor = new RendererTrackIndexExtractor();
    }

    @Test
    public void givenAudioTrackAtPositionZero_whenExtractingAudioIndex_thenReturnsIndexZero() {
        int audioIndex = extractor.get(TrackType.AUDIO, 1, rendererTypeRequesterAudioTrack);
        int expectedAudioIndex = 0;

        assertThat(audioIndex).isEqualTo(expectedAudioIndex);
    }

    @Test
    public void givenVideoTrackAtPositionZero_whenExtractingVideoIndex_thenReturnsIndexZero() {
        int videoIndex = extractor.get(TrackType.VIDEO, 1, rendererTypeRequesterVideoTrack);
        int expectedVideoIndex = 0;

        assertThat(videoIndex).isEqualTo(expectedVideoIndex);
    }

    @Test
    public void givenSubtitlesTrackAtPositionZero_whenExtractingTextIndex_thenReturnsIndexZero() {
        int textIndex = extractor.get(TrackType.TEXT, 1, rendererTypeRequesterTextTrack);
        int expectedTextIndex = 0;

        assertThat(textIndex).isEqualTo(expectedTextIndex);
    }

    @Test
    public void givenThreeTrackTypes_whenExtractingAudioIndexes_thenReturnsIndexOne() {
        int audioIndex = extractor.get(TrackType.AUDIO, 3, rendererTypeRequesterVideoAudioTextTrack);
        int expectedAudioIndex = 1;

        assertThat(audioIndex).isEqualTo(expectedAudioIndex);
    }

    private RendererTypeRequester rendererTypeRequesterAudioTrack = new RendererTypeRequester() {
        @Override
        public int getRendererTypeFor(int index) {
            if (index == 0) {
                return C.TRACK_TYPE_AUDIO;
            }

            return -1;
        }
    };

    private RendererTypeRequester rendererTypeRequesterVideoTrack = new RendererTypeRequester() {
        @Override
        public int getRendererTypeFor(int index) {
            if (index == 0) {
                return C.TRACK_TYPE_VIDEO;
            }

            return -1;
        }
    };

    private RendererTypeRequester rendererTypeRequesterTextTrack = new RendererTypeRequester() {
        @Override
        public int getRendererTypeFor(int index) {
            if (index == 0) {
                return C.TRACK_TYPE_TEXT;
            }

            return -1;
        }
    };

    private RendererTypeRequester rendererTypeRequesterVideoAudioTextTrack = new RendererTypeRequester() {
        @Override
        public int getRendererTypeFor(int index) {
            switch (index) {
                case 0:
                    return C.TRACK_TYPE_VIDEO;
                case 1:
                    return C.TRACK_TYPE_AUDIO;
                case 2:
                    return C.TRACK_TYPE_TEXT;
                default:
                    return -1;
            }
        }
    };
}
