package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerVideoTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerAudioTrackFixture;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.noplayer.model.PlayerVideoTrackFixture;
import com.novoda.noplayer.model.VideoDuration;
import com.novoda.utils.Optional;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import utils.ExceptionMatcher;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class ExoPlayerFacadeTest {

    private static final boolean SELECTED = true;
    private static final PlayerVideoTrack PLAYER_VIDEO_TRACK = PlayerVideoTrackFixture.aPlayerVideoTrack().build();

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

        private static final long ANY_POSITION = 1000;
        private static final PlayerAudioTrack PLAYER_AUDIO_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().build();
        private static final AudioTracks AUDIO_TRACKS = AudioTracks.from(Collections.singletonList(PLAYER_AUDIO_TRACK));

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void whenResetting_thenReleasesUnderlyingPlayer() {

            facade.release();

            verify(exoPlayer, never()).release();
        }

        @Test
        public void whenLoadingVideo_thenAddsPlayerEventListener() {

            facade.loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, exoPlayerForwarder, mediaCodecSelector);

            verify(exoPlayer).addListener(exoPlayerForwarder.exoPlayerEventListener());
        }

        @Test
        public void whenLoadingVideo_thenSetsVideoDebugListener() {

            facade.loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, exoPlayerForwarder, mediaCodecSelector);

            verify(exoPlayer).setVideoDebugListener(exoPlayerForwarder.videoRendererEventListener());
        }

        @Test
        public void whenLoadingVideo_thenSetsSurfaceHolderOnExoPlayer() {

            facade.loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, exoPlayerForwarder, mediaCodecSelector);

            verify(exoPlayer).setVideoSurfaceHolder(surfaceHolder);
        }

        @Test
        public void givenMediaSource_whenLoadingVideo_thenPreparesInternalExoPlayer() {
            MediaSource mediaSource = givenMediaSource();

            facade.loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, exoPlayerForwarder, mediaCodecSelector);

            verify(exoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
        }

        @Test
        public void whenQueryingIsPlaying_thenReturnsFalse() {

            boolean isPlaying = facade.isPlaying();

            assertThat(isPlaying).isFalse();
        }

        @Test
        public void whenQueryingPlayheadPosition_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.playheadPositionInMillis();
        }

        @Test
        public void whenQueryingMediaDuration_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.getMediaDuration();
        }

        @Test
        public void whenQueryingBufferPercentage_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.getBufferPercentage();
        }

        @Test
        public void whenPausing_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.pause();
        }

        @Test
        public void whenSeeking_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.seekTo(ANY_POSITION);
        }

        @Test
        public void whenSelectingAudioTrack_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

            facade.selectAudioTrack(audioTrack);
        }

        @Test
        public void whenGettingAudioTracks_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            given(audioTrackSelector.getAudioTracks(any(RendererTypeRequester.class))).willReturn(AUDIO_TRACKS);

            facade.getAudioTracks();
        }

        @Test
        public void selectSubtitleTrack_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);

            facade.selectSubtitleTrack(subtitleTrack);
        }
    }

    public static class GivenVideoIsLoaded extends Base {

        private static final PlayerAudioTrack PLAYER_AUDIO_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().build();
        private static final AudioTracks AUDIO_TRACKS = AudioTracks.from(Collections.singletonList(PLAYER_AUDIO_TRACK));
        private static final PlayerVideoTrack PLAYER_VIDEO_TRACK = PlayerVideoTrackFixture.aPlayerVideoTrack().build();
        private static final List<PlayerVideoTrack> VIDEO_TRACKS = Collections.singletonList(PLAYER_VIDEO_TRACK);

        @Override
        public void setUp() {
            super.setUp();
            givenPlayerIsLoaded();
        }

        private void givenPlayerIsLoaded() {
            givenMediaSource();
            facade.loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, exoPlayerForwarder, mediaCodecSelector);
        }

        @Test
        public void whenResetting_thenReleasesUnderlyingPlayer() {
            facade.release();

            verify(exoPlayer).release();
        }

        @Test
        public void whenPausing_thenSetsPlayWhenReadyToFalse() {

            facade.pause();

            verify(exoPlayer).setPlayWhenReady(DO_NOT_PLAY_WHEN_READY);
        }

        @Test
        public void whenSeeking_thenSeeksToPosition() {
            long videoPositionInMillis = TWO_MINUTES_IN_MILLIS;

            facade.seekTo(videoPositionInMillis);

            verify(exoPlayer).seekTo(videoPositionInMillis);
        }

        @Test
        public void whenStartingPlay_thenSetsPlayWhenReadyToTrue() {

            facade.play();

            verify(exoPlayer).setPlayWhenReady(PLAY_WHEN_READY);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSeeksToPosition() {
            facade.play(TWO_MINUTES_IN_MILLIS);

            verify(exoPlayer).seekTo(TWO_MINUTES_IN_MILLIS);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSetsPlayWhenReadyToTrue() {
            facade.play(TWO_MINUTES_IN_MILLIS);

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

            long playheadPositionInMillis = facade.playheadPositionInMillis();

            assertThat(playheadPositionInMillis).isEqualTo(TWO_MINUTES_IN_MILLIS);
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

        @Test
        public void whenSelectingAudioTrack_thenDelegatesToTrackSelector() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

            facade.selectAudioTrack(audioTrack);

            verify(audioTrackSelector).selectAudioTrack(audioTrack, rendererTypeRequester);
        }

        @Test
        public void givenSelectingAudioTrackSuceeds_whenSelectingAudioTrack_thenReturnsTrue() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);
            given(audioTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester)).willReturn(true);

            boolean success = facade.selectAudioTrack(audioTrack);

            assertThat(success).isTrue();
        }

        @Test
        public void givenSelectingAudioTrackFails_whenSelectingAudioTrack_thenReturnsFalse() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);
            given(audioTrackSelector.selectAudioTrack(audioTrack, rendererTypeRequester)).willReturn(false);

            boolean success = facade.selectAudioTrack(audioTrack);

            assertThat(success).isFalse();
        }

        @Test
        public void whenSelectingSubtitlesTrack_thenDelegatesToTrackSelector() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);

            facade.selectSubtitleTrack(subtitleTrack);

            verify(subtitleTrackSelector).selectTextTrack(subtitleTrack, rendererTypeRequester);
        }

        @Test
        public void givenSelectingTextTrackSuceeds_whenSelectingSubtitlesTrack_thenReturnsTrue() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);
            given(subtitleTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester)).willReturn(true);

            boolean success = facade.selectSubtitleTrack(subtitleTrack);

            assertThat(success).isTrue();
        }

        @Test
        public void givenSelectingTextTrackFails_whenSelectingSubtitlesTrack_thenReturnsFalse() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);
            given(subtitleTrackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester)).willReturn(false);

            boolean success = facade.selectSubtitleTrack(subtitleTrack);

            assertThat(success).isFalse();
        }

        @Test
        public void whenGettingAudioTracks_thenDelegatesToTrackSelector() {
            given(audioTrackSelector.getAudioTracks(any(RendererTypeRequester.class))).willReturn(AUDIO_TRACKS);

            AudioTracks audioTracks = facade.getAudioTracks();

            assertThat(audioTracks).isEqualTo(AUDIO_TRACKS);
        }

        @Test
        public void whenGettingSelectedVideoTrack_thenDelegatesTrackSelector() {
            given(videoTrackSelector.getSelectedVideoTrack(eq(exoPlayer), any(RendererTypeRequester.class), any(ContentType.class))).willReturn(Optional.of(PLAYER_VIDEO_TRACK));

            Optional<PlayerVideoTrack> selectedVideoTrack = facade.getSelectedVideoTrack();

            assertThat(selectedVideoTrack).isEqualTo(Optional.of(PLAYER_VIDEO_TRACK));
        }

        @Test
        public void whenSelectingVideoTrack_thenDelegatesToTrackSelector() {
            given(videoTrackSelector.selectVideoTrack(eq(PLAYER_VIDEO_TRACK), any(RendererTypeRequester.class))).willReturn(SELECTED);

            boolean selectedVideoTrack = facade.selectVideoTrack(PLAYER_VIDEO_TRACK);

            assertThat(selectedVideoTrack).isTrue();
        }

        @Test
        public void whenGettingVideoTracks_thenDelegatesToTrackSelector() {
            given(videoTrackSelector.getVideoTracks(any(RendererTypeRequester.class), any(ContentType.class))).willReturn(VIDEO_TRACKS);

            List<PlayerVideoTrack> videoTracks = facade.getVideoTracks();

            assertThat(videoTracks).isEqualTo(VIDEO_TRACKS);
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
        ExoPlayerAudioTrackSelector audioTrackSelector;
        @Mock
        ExoPlayerSubtitleTrackSelector subtitleTrackSelector;
        @Mock
        ExoPlayerVideoTrackSelector videoTrackSelector;
        @Mock
        Uri uri;
        @Mock
        RendererTypeRequester rendererTypeRequester;
        @Mock
        RendererTypeRequesterCreator rendererTypeRequesterCreator;
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        DefaultDrmSessionManager.EventListener drmSessionEventListener;
        @Mock
        MediaCodecSelector mediaCodecSelector;
        @Mock
        SurfaceHolder surfaceHolder;

        ExoPlayerFacade facade;

        @Before
        public void setUp() {
            ExoPlayerCreator exoPlayerCreator = mock(ExoPlayerCreator.class);
            given(exoPlayerForwarder.drmSessionEventListener()).willReturn(drmSessionEventListener);
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, mediaCodecSelector)).willReturn(exoPlayer);
            when(rendererTypeRequesterCreator.createfrom(exoPlayer)).thenReturn(rendererTypeRequester);
            facade = new ExoPlayerFacade(
                    mediaSourceFactory,
                    audioTrackSelector,
                    subtitleTrackSelector,
                    videoTrackSelector,
                    exoPlayerCreator,
                    rendererTypeRequesterCreator);
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
