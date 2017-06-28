package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(Enclosed.class)
public class PlaybackStateCheckerTest {

    private static final MediaPlayer ANY_MEDIA_PLAYER = mock(MediaPlayer.class);
    private static final MediaPlayer NO_MEDIA_PLAYER = null;
    private static final boolean IS_IN_PLAYBACK_STATE = true;
    private static final boolean IS_NOT_IN_PLAYBACK_STATE = false;

    @RunWith(Parameterized.class)
    public static class CheckingPlaybackState {

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        private final MediaPlayer mediaPlayer;
        private final PlaybackStateChecker.PlaybackState playbackState;
        private final boolean expectedIsInPlaybackState;

        @Parameterized.Parameters(name = "MediaPlayer: {0} Playback state: {1}, isInPlaybackState: {2}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(
                    new Object[]{NO_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.COMPLETED, IS_NOT_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.ERROR, IS_NOT_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.IDLE, IS_NOT_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.PREPARING, IS_NOT_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.PREPARED, IS_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.PLAYING, IS_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.PAUSED, IS_IN_PLAYBACK_STATE},
                    new Object[]{ANY_MEDIA_PLAYER, PlaybackStateChecker.PlaybackState.COMPLETED, IS_IN_PLAYBACK_STATE}
            );
        }

        public CheckingPlaybackState(MediaPlayer mediaPlayer,
                                     PlaybackStateChecker.PlaybackState playbackState,
                                     boolean expectedIsInPlaybackState) {
            this.mediaPlayer = mediaPlayer;
            this.playbackState = playbackState;
            this.expectedIsInPlaybackState = expectedIsInPlaybackState;
        }

        @Test
        public void whenCheckingIsInPlaybackState_thenReturnsExpectedState() {
            PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();

            boolean inPlaybackState = playbackStateChecker.isInPlaybackState(mediaPlayer, playbackState);

            assertThat(inPlaybackState).isEqualTo(expectedIsInPlaybackState);
        }
    }

    @RunWith(Parameterized.class)
    public static class CheckingIsPlaying {

        private static final boolean MEDIA_PLAYER_IS_PLAYING = true;
        private static final boolean MEDIA_PLAYER_IS_NOT_PLAYING = false;
        private static final boolean IS_PLAYING = true;
        private static final boolean IS_NOT_PLAYING = false;

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        private final MediaPlayer mediaPlayer;
        private final PlaybackStateChecker.PlaybackState playbackState;
        private final boolean expectedIsPlaying;

        @Parameterized.Parameters(name = "MediaPlayer: {0} mediaPlayer.isPlaying(): {1} Playback state: {2}, isPlaying: {3}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(
                    new Object[]{NO_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.COMPLETED, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.ERROR, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.IDLE, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.PREPARING, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.PREPARED, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.PLAYING, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.PAUSED, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_PLAYING, PlaybackStateChecker.PlaybackState.COMPLETED, IS_PLAYING},

                    new Object[]{NO_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.COMPLETED, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.ERROR, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.IDLE, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.PREPARING, IS_NOT_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.PREPARED, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.PLAYING, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.PAUSED, IS_PLAYING},
                    new Object[]{ANY_MEDIA_PLAYER, MEDIA_PLAYER_IS_NOT_PLAYING, PlaybackStateChecker.PlaybackState.COMPLETED, IS_PLAYING}
            );
        }

        public CheckingIsPlaying(MediaPlayer mediaPlayer,
                                 boolean mediaPlayerIsPlaying,
                                 PlaybackStateChecker.PlaybackState playbackState,
                                 boolean expectedIsPlaying) {
            this.mediaPlayer = mediaPlayer;
            this.playbackState = playbackState;
            this.expectedIsPlaying = expectedIsPlaying;
            if (mediaPlayer != null) {
                given(mediaPlayer.isPlaying()).willReturn(mediaPlayerIsPlaying);
            }
        }

        @Test
        public void whenCheckingIsPlaying_thenReturnsExpectedState() {
            PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();

            boolean inPlaybackState = playbackStateChecker.isInPlaybackState(mediaPlayer, playbackState);

            assertThat(inPlaybackState).isEqualTo(expectedIsPlaying);
        }
    }
}
