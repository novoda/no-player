package com.novoda.noplayer.internal.exoplayer.mediasource;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.internal.exoplayer.RendererTypeRequester;
import com.novoda.noplayer.internal.utils.Optional;

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
        Optional<Integer> audioIndex = extractor.extract(TrackType.AUDIO, 1, rendererTypeRequesterAudioTrack);
        int expectedAudioIndex = 0;

        assertThat(audioIndex.get()).isEqualTo(expectedAudioIndex);
    }

    @Test
    public void givenVideoTrackAtPositionZero_whenExtractingVideoIndex_thenReturnsIndexZero() {
        Optional<Integer> videoIndex = extractor.extract(TrackType.VIDEO, 1, rendererTypeRequesterVideoTrack);
        int expectedVideoIndex = 0;

        assertThat(videoIndex.get()).isEqualTo(expectedVideoIndex);
    }

    @Test
    public void givenSubtitlesTrackAtPositionZero_whenExtractingTextIndex_thenReturnsIndexZero() {
        Optional<Integer> textIndex = extractor.extract(TrackType.TEXT, 1, rendererTypeRequesterTextTrack);
        int expectedTextIndex = 0;

        assertThat(textIndex.get()).isEqualTo(expectedTextIndex);
    }

    @Test
    public void givenThreeTrackTypes_whenExtractingAudioIndexes_thenReturnsIndexOne() {
        Optional<Integer> audioIndex = extractor.extract(TrackType.AUDIO, 3, rendererTypeRequesterVideoAudioTextTrack);
        int expectedAudioIndex = 1;

        assertThat(audioIndex.get()).isEqualTo(expectedAudioIndex);
    }

    @Test
    public void givenNoAudioTrack_whenExtractingAudioIndex_thenReturnsEmpty() {
        Optional<Integer> audioIndex = extractor.extract(TrackType.AUDIO, 1, emptyRendererTypeRequester);

        assertThat(audioIndex.isAbsent()).isTrue();
    }

    @Test
    public void givenNoVideoTrack_whenExtractingVideoIndex_thenReturnsEmpty() {
        Optional<Integer> videoIndex = extractor.extract(TrackType.VIDEO, 1, emptyRendererTypeRequester);

        assertThat(videoIndex.isAbsent()).isTrue();
    }

    @Test
    public void givenNoTextTrack_whenExtractingTextIndex_thenReturnsEmpty() {
        Optional<Integer> textIndex = extractor.extract(TrackType.TEXT, 1, emptyRendererTypeRequester);

        assertThat(textIndex.isAbsent()).isTrue();
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

    private RendererTypeRequester emptyRendererTypeRequester = new RendererTypeRequester() {
        @Override
        public int getRendererTypeFor(int index) {
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
