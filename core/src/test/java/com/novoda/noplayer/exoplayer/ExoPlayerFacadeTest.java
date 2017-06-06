package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class ExoPlayerFacadeTest {

    private static final long TWO_MINUTES_IN_MILLIS = 120000;
    private static final long TEN_MINUTES_IN_MILLIS = 600000;

    private static final int TEN_PERCENT = 10;

    private static final boolean IS_PLAYING = true;
    private static final boolean PLAY_WHEN_READY = true;
    private static final boolean DO_NOT_PLAY_WHEN_READY = false;
    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private static final ContentType ANY_CONTENT_TYPE = ContentType.DASH;

    public static class GivenVideoNotLoaded extends Base {

        private static final VideoPosition ANY_POSITION = VideoPosition.fromMillis(1000);

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void whenResetting_thenReleasesUnderlyingPlayer() {

            facade.reset();

            verify(exoPlayer, never()).release();
        }

        @Test
        public void whenLoadingVideo_thenAddsPlayerEventListener() {

            facade.loadVideo(uri, ANY_CONTENT_TYPE, exoPlayerForwarder);

            verify(exoPlayer).addListener(exoPlayerForwarder.exoPlayerEventListener());
        }

        @Test
        public void whenLoadingVideo_thenSetsVideoDebugListener() {

            facade.loadVideo(uri, ANY_CONTENT_TYPE, exoPlayerForwarder);

            verify(exoPlayer).setVideoDebugListener(exoPlayerForwarder.videoRendererEventListener());
        }

        @Test
        public void givenMediaSource_whenLoadingVideo_thenPreparesInternalExoPlayer() {
            MediaSource mediaSource = givenMediaSource();

            facade.loadVideo(uri, ANY_CONTENT_TYPE, exoPlayerForwarder);

            verify(exoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
        }

        @Test
        public void whenSelectingAudioTrack_thenDelegatesToTrackSelector() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

            facade.selectAudioTrack(audioTrack);

            verify(trackSelector).selectAudioTrack(audioTrack);
        }

        @Test
        public void whenGettingAudioTracks_thenDelegatesToTrackSelector() {

            facade.getAudioTracks();

            verify(trackSelector).getAudioTracks();
        }

        @Test
        public void whenQueryingIsPlaying_thenReturnsFalse() {

            boolean isPlaying = facade.isPlaying();

            assertThat(isPlaying).isFalse();
        }

        @Test
        public void whenQueryingPlayheadPosition_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.getPlayheadPosition();
        }

        @Test
        public void whenQueryingMediaDuration_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.getMediaDuration();
        }

        @Test
        public void whenQueryingBufferPercentage_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.getBufferPercentage();
        }

        @Test
        public void whenPausing_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.pause();
        }

        @Test
        public void whenSeeking_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.seekTo(ANY_POSITION);
        }

        @Test
        public void whenStopping_thenThrowsIllegalStateException() {
            thrown.expect(IllegalStateException.class);

            facade.stop();
        }
    }

    public static class GivenVideoIsLoaded extends Base {

        @Override
        public void setUp() {
            super.setUp();
            givenPlayerIsLoaded();
        }

        private void givenPlayerIsLoaded() {
            givenMediaSource();
            facade.loadVideo(uri, ANY_CONTENT_TYPE, exoPlayerForwarder);
        }

        @Test
        public void whenResetting_thenReleasesUnderlyingPlayer() {
            facade.reset();

            verify(exoPlayer).release();
        }

        @Test
        public void whenPausing_thenSetsPlayWhenReadyToFalse() {

            facade.pause();

            verify(exoPlayer).setPlayWhenReady(DO_NOT_PLAY_WHEN_READY);
        }

        @Test
        public void whenSeeking_thenSeeksToPosition() {
            VideoPosition videoPosition = VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS);

            facade.seekTo(videoPosition);

            verify(exoPlayer).seekTo(videoPosition.inMillis());
        }

        @Test
        public void whenStopping_thenStopsPlayer() {

            facade.stop();

            verify(exoPlayer).stop();
        }

        @Test
        public void whenStartingPlayback_thenClearsAndSetsVideoSurfaceHolder() {

            facade.play(surfaceHolder);

            InOrder inOrder = inOrder(exoPlayer);
            inOrder.verify(exoPlayer).clearVideoSurfaceHolder(surfaceHolder);
            inOrder.verify(exoPlayer).setVideoSurfaceHolder(surfaceHolder);
        }

        @Test
        public void whenStartingPlay_thenSetsPlayWhenReadyToTrue() {

            facade.play(surfaceHolder);

            verify(exoPlayer).setPlayWhenReady(PLAY_WHEN_READY);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSeeksToPosition() {

            facade.play(surfaceHolder, VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(exoPlayer).seekTo(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS).inMillis());
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSetsPlayWhenReadyToTrue() {

            facade.play(surfaceHolder, VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(exoPlayer).setPlayWhenReady(PLAY_WHEN_READY);
        }

        @Test
        public void givenExoPlayerIsReadyToPlay_whenQueryingIsPlaying_thenReturnsTrue() {
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);

            boolean isPlaying = facade.isPlaying();

            assertThat(isPlaying).isTrue();
        }

        @Test
        public void whenGettingPlayheadPosition_thenReturnsCurrentPosition() {
            given(exoPlayer.getCurrentPosition()).willReturn(TWO_MINUTES_IN_MILLIS);

            VideoPosition playheadPosition = facade.getPlayheadPosition();

            assertThat(playheadPosition).isEqualTo(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));
        }

        @Test
        public void whenGettingMediaDuration_thenReturnsDuration() {
            given(exoPlayer.getDuration()).willReturn(TEN_MINUTES_IN_MILLIS);

            VideoDuration videoDuration = facade.getMediaDuration();

            assertThat(videoDuration).isEqualTo(VideoDuration.fromMillis(TEN_MINUTES_IN_MILLIS));
        }

        @Test
        public void whenGettingBufferPercentage_thenReturnsBufferPercentage() {
            given(exoPlayer.getBufferedPercentage()).willReturn(TEN_PERCENT);

            int bufferPercentage = facade.getBufferPercentage();

            assertThat(bufferPercentage).isEqualTo(TEN_PERCENT);
        }
    }

    public abstract static class Base {

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        SimpleExoPlayer exoPlayer;
        @Mock
        MediaSourceFactory mediaSourceFactory;
        @Mock
        ExoPlayerForwarder exoPlayerForwarder;
        @Mock
        ExoPlayerAudioTrackSelector trackSelector;
        @Mock
        Uri uri;
        @Mock
        SurfaceHolder surfaceHolder;

        ExoPlayerFacade facade;

        @Before
        public void setUp() {
            ExoPlayerCreator exoPlayerCreator = mock(ExoPlayerCreator.class);
            given(exoPlayerCreator.create()).willReturn(exoPlayer);
            facade = new ExoPlayerFacade(
                    mediaSourceFactory,
                    trackSelector,
                    exoPlayerCreator
            );
        }

        MediaSource givenMediaSource() {
            MediaSource mediaSource = mock(MediaSource.class);
            given(
                    mediaSourceFactory.create(
                            ANY_CONTENT_TYPE,
                            uri,
                            exoPlayerForwarder.extractorMediaSourceListener(),
                            exoPlayerForwarder.mediaSourceEventListener()
                    )
            ).willReturn(mediaSource);

            return mediaSource;
        }
    }
}
