package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.SinglePeriodAdTimeline;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerState;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerAudioTrackFixture;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;
import com.novoda.noplayer.model.PlayerVideoTrackFixture;
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
import utils.ExceptionMatcher;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class ExoPlayerFacadeTest {

    private static final boolean SELECTED = true;

    private static final long TWENTY_FIVE_SECONDS_IN_MILLIS = 25000;
    private static final long TWO_MINUTES_IN_MILLIS = 120000;
    private static final long TEN_MINUTES_IN_MILLIS = 600000;
    private static final long MICROS = 1000;
    private static final long[] ADVERT_DURATIONS = {10 * MICROS, 20 * MICROS, 30 * MICROS, 40 * MICROS};
    private static final long NO_RESUME_POSITION = 0;

    private static final int TEN_PERCENT = 10;

    private static final boolean IS_PLAYING = true;
    private static final boolean IS_NOT_PLAYING = false;
    private static final boolean PLAY_WHEN_READY = true;
    private static final boolean DO_NOT_PLAY_WHEN_READY = false;
    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_POSITION = false;
    private static final boolean DO_NOT_RESET_STATE = false;

    private static final Options OPTIONS = new OptionsBuilder()
            .withContentType(ContentType.DASH)
            .build();

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

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer).addListener(exoPlayerForwarder.exoPlayerEventListener());
        }

        @Test
        public void whenLoadingVideo_thenSetsAnalyticsListener() {

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer).addAnalyticsListener(exoPlayerForwarder.analyticsListener());
        }

        @Test
        public void givenAdsLoader_andListener_whenLoadingVideo_thenBindsAdvertListener() {
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);
            given(optionalAdvertListener.isPresent()).willReturn(true);
            given(optionalAdvertListener.get()).willReturn(advertListener);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader).bind(optionalAdvertListener, NO_RESUME_POSITION, NO_RESUME_POSITION);
        }

        @Test
        public void givenAdsLoader_butAbsentListener_whenLoadingVideo_thenBindsAdvertListener() {
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);
            given(optionalAdvertListener.isPresent()).willReturn(false);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader).bind(exoPlayerForwarder.advertListener(), NO_RESUME_POSITION, NO_RESUME_POSITION);
        }

        @Test
        public void givenAdsLoader_whenLoadingVideo_thenSetsPlayerOnAdLoader() {
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader).setPlayer(exoPlayer);
        }

        @Test
        public void givenAdsLoader_whenReleasing_thenReleasesAdsLoader() {
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);

            facade.release();

            verify(adsLoader).release();
        }

        @Test
        public void givenAbsentAdsLoader_butPresentListener_whenLoadingVideo_thenDoesNotBindAdvertListener() {
            given(optionalAdvertListener.isPresent()).willReturn(true);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader, never()).bind(exoPlayerForwarder.advertListener(), NO_RESUME_POSITION, NO_RESUME_POSITION);
        }

        @Test
        public void givenAbsentAdsLoader_whenLoadingVideo_thenDoesNotBindAdvertListener() {

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader, never()).bind(exoPlayerForwarder.advertListener(), NO_RESUME_POSITION, NO_RESUME_POSITION);
        }

        @Test
        public void givenInitialAdvertBreakPosition_whenLoadingVideo_thenBindsAdvertListenerWithResumePosition() {
            Options options = OPTIONS.toOptionsBuilder()
                    .withInitialAdvertBreakPositionInMillis(TWENTY_FIVE_SECONDS_IN_MILLIS)
                    .build();
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, allowFallbackDecoder, requiresSecureDecoder, options, trackSelector.trackSelector())).willReturn(exoPlayer);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, options, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader).bind(exoPlayerForwarder.advertListener(), TWENTY_FIVE_SECONDS_IN_MILLIS, NO_RESUME_POSITION);
        }

        @Test
        public void givenInitialContentPosition_whenLoadingVideo_thenBindsAdvertListenerWithInitialContentPosition() {
            Options options = OPTIONS.toOptionsBuilder()
                    .withInitialPositionInMillis(TWENTY_FIVE_SECONDS_IN_MILLIS)
                    .build();
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, allowFallbackDecoder, requiresSecureDecoder, options, trackSelector.trackSelector())).willReturn(exoPlayer);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, options, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader).bind(exoPlayerForwarder.advertListener(), NO_RESUME_POSITION, TWENTY_FIVE_SECONDS_IN_MILLIS);
        }

        @Test
        public void givenAbsentAdsLoader_whenLoadingVideo_thenDoesNotSetPlayerOnAdLoader() {

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(adsLoader, never()).setPlayer(exoPlayer);
        }

        @Test
        public void givenAbsentAdsLoader_whenReleasing_thenDoesNotReleaseAdsLoader() {

            facade.release();

            verify(adsLoader, never()).release();
        }

        @Test
        public void givenSurfaceContainerContainsSurfaceView_whenLoadingVideo_thenSetsSurfaceViewOnExoPlayer() {

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer).setVideoSurfaceView(surfaceView);
        }

        @Test
        public void givenSurfaceContainerContainsTextureView_whenLoadingVideo_thenSetsTextureViewOnExoPlayer() {

            facade.loadVideo(textureViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer).setVideoTextureView(textureView);
        }

        @Test
        public void givenLollipopDevice_whenLoadingVideo_thenSetsMovieAudioAttributesOnExoPlayer() {
            given(androidDeviceVersion.isLollipopTwentyOneOrAbove()).willReturn(true);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            AudioAttributes expectedMovieAudioAttributes = new AudioAttributes.Builder()
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build();
            verify(exoPlayer).setAudioAttributes(expectedMovieAudioAttributes);
        }

        @Test
        public void givenNonLollipopDevice_whenLoadingVideo_thenDoesNotSetAudioAttributesOnExoPlayer() {
            given(androidDeviceVersion.isLollipopTwentyOneOrAbove()).willReturn(false);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer, never()).setAudioAttributes(any(AudioAttributes.class));
        }

        @Test
        public void givenMediaSource_whenLoadingVideo_thenPreparesInternalExoPlayer() {
            MediaSource mediaSource = givenMediaSource(OPTIONS);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            verify(exoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
        }

        @Test
        public void givenInitialPosition_whenLoadingVideo_thenPerformsSeekBeforePreparing() {
            Options options = OPTIONS.toOptionsBuilder()
                    .withInitialPositionInMillis(TWENTY_FIVE_SECONDS_IN_MILLIS)
                    .build();
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, allowFallbackDecoder, requiresSecureDecoder, options, trackSelector.trackSelector())).willReturn(exoPlayer);
            MediaSource mediaSource = givenMediaSource(options);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, options, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            InOrder inOrder = inOrder(exoPlayer);
            inOrder.verify(exoPlayer).seekTo(TWENTY_FIVE_SECONDS_IN_MILLIS);
            inOrder.verify(exoPlayer).prepare(mediaSource, DO_NOT_RESET_POSITION, DO_NOT_RESET_STATE);
        }

        @Test
        public void givenNoInitialPosition_whenLoadingVideo_thenDoesNotPerformSeekBeforePreparing() {
            MediaSource mediaSource = givenMediaSource(OPTIONS);

            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);

            InOrder inOrder = inOrder(exoPlayer);
            inOrder.verify(exoPlayer, never()).seekTo(TWENTY_FIVE_SECONDS_IN_MILLIS);
            inOrder.verify(exoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
        }

        @Test
        public void whenQueryingIsPlaying_thenReturnsFalse() {

            boolean isPlaying = facade.isPlaying();

            assertThat(isPlaying).isFalse();
        }

        @Test
        public void whenQueryingVideoType_thenReturnsUndefined() {

            PlayerState.VideoType videoType = facade.videoType();

            assertThat(videoType).isEqualTo(PlayerState.VideoType.UNDEFINED);
        }

        @Test
        public void whenQueryingPlayheadPosition_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.playheadPositionInMillis();
        }

        @Test
        public void whenQueryingMediaDuration_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.mediaDurationInMillis();
        }

        @Test
        public void whenQueryingContentDuration_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.contentDurationInMillis();
        }

        @Test
        public void whenQueryingAdvertBreakDuration_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.advertBreakDurationInMillis();
        }

        @Test
        public void whenQueryingPositionInAdvertBreak_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.positionInAdvertBreakInMillis();
        }

        @Test
        public void whenQueryingBufferPercentage_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.bufferPercentage();
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

            given(trackSelector.getAudioTracks(any(RendererTypeRequester.class))).willReturn(AUDIO_TRACKS);

            facade.getAudioTracks();
        }

        @Test
        public void selectSubtitleTrack_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);

            facade.selectSubtitleTrack(subtitleTrack);
        }

        @Test
        public void whenSetVolume_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.setVolume(ANY_VOLUME);
        }

        @Test
        public void whenGetVolume_thenThrowsIllegalStateException() {
            thrown.expect(ExceptionMatcher.matches("Video must be loaded before trying to interact with the player", IllegalStateException.class));

            facade.getVolume();
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
            givenMediaSource(OPTIONS);
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, allowFallbackDecoder, requiresSecureDecoder, OPTIONS, trackSelector.trackSelector())).willReturn(exoPlayer);
            facade.loadVideo(surfaceViewHolder, drmSessionCreator, uri, OPTIONS, exoPlayerForwarder, allowFallbackDecoder, requiresSecureDecoder);
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
        public void givenExoPlayerTimelineIsEmpty_whenQueryingVideoType_thenReturnsUndefined() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(timeline.isEmpty()).willReturn(true);

            PlayerState.VideoType videoType = facade.videoType();

            assertThat(videoType).isEqualTo(PlayerState.VideoType.UNDEFINED);
        }

        @Test
        public void givenExoPlayerIsPlayingAd_whenQueryingVideoType_thenReturnsAdvert() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);

            PlayerState.VideoType videoType = facade.videoType();

            assertThat(videoType).isEqualTo(PlayerState.VideoType.ADVERT);
        }

        @Test
        public void givenExoPlayerIsNotPlayingAd_whenQueryingVideoType_thenReturnsContent() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_NOT_PLAYING);

            PlayerState.VideoType videoType = facade.videoType();

            assertThat(videoType).isEqualTo(PlayerState.VideoType.CONTENT);
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

            long videoDurationInMillis = facade.mediaDurationInMillis();

            assertThat(videoDurationInMillis).isEqualTo(TEN_MINUTES_IN_MILLIS);
        }

        @Test
        public void whenGettingContentDuration_thenReturnsDuration() {
            given(exoPlayer.getContentDuration()).willReturn(TEN_MINUTES_IN_MILLIS);

            long contentDurationInMillis = facade.contentDurationInMillis();

            assertThat(contentDurationInMillis).isEqualTo(TEN_MINUTES_IN_MILLIS);
        }

        @Test
        public void whenGettingAdvertBreakDuration_thenReturnsDurationOfAllAdsInTheBreak() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            long[] durations = {100 * MICROS, 200 * MICROS, 300 * MICROS, 400 * MICROS};
            givenAdGroupAtPositionContainsAdsWithDurations(2, durations);

            long advertBreakDurationInMillis = facade.advertBreakDurationInMillis();

            assertThat(advertBreakDurationInMillis).isEqualTo(1000);
        }

        @Test
        public void whenGettingAdvertBreakDuration_andDurationIsUnset_thenUsesDurationFromAdsLoader() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);
            long[] durations = {100 * MICROS, 200 * MICROS, C.TIME_UNSET, 400 * MICROS};
            givenAdGroupAtPositionContainsAdsWithDurations(2, durations);
            given(adsLoader.advertDurationBy(2, 2)).willReturn(800 * MICROS);

            long advertBreakDurationInMillis = facade.advertBreakDurationInMillis();

            assertThat(advertBreakDurationInMillis).isEqualTo(1500);
        }

        @Test
        public void whenGettingAdvertBreakDuration_andAdvertIsNotPlaying_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_NOT_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);

            long advertBreakDurationInMillis = facade.advertBreakDurationInMillis();

            assertThat(advertBreakDurationInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingAdvertBreakDuration_andPlayerIsNotReady_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_NOT_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);

            long advertBreakDurationInMillis = facade.advertBreakDurationInMillis();

            assertThat(advertBreakDurationInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingAdvertBreakDuration_andAdvertLoaderIsMissing_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_NOT_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(false);

            long advertBreakDurationInMillis = facade.advertBreakDurationInMillis();

            assertThat(advertBreakDurationInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingPositionInAdvertBreak_thenReturnsDurationOfPreviousAdsInBreakWithCurrentPlayheadPosition() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            long[] durations = {100 * MICROS, 200 * MICROS, 300 * MICROS, 400 * MICROS};
            givenAdGroupAtPositionContainsAdsWithDurations(1, durations);
            given(exoPlayer.getCurrentPosition()).willReturn(150L);
            given(exoPlayer.getCurrentAdIndexInAdGroup()).willReturn(2);

            long positionInAdvertBreakInMillis = facade.positionInAdvertBreakInMillis();

            assertThat(positionInAdvertBreakInMillis).isEqualTo(450);
        }

        @Test
        public void whenGettingPositionInAdvertBreak_andDurationIsUnset_thenUsesDurationFromAdsLoader() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);
            given(optionalAdsLoader.get()).willReturn(adsLoader);
            long[] durations = {100 * MICROS, 200 * MICROS, C.TIME_UNSET, 400 * MICROS};
            givenAdGroupAtPositionContainsAdsWithDurations(2, durations);
            given(adsLoader.advertDurationBy(2, 2)).willReturn(800 * MICROS);
            given(exoPlayer.getCurrentPosition()).willReturn(150L);
            given(exoPlayer.getCurrentAdIndexInAdGroup()).willReturn(3);

            long positionInAdvertBreakInMillis = facade.positionInAdvertBreakInMillis();

            assertThat(positionInAdvertBreakInMillis).isEqualTo(1250);
        }

        @Test
        public void whenGettingPositionInAdvertBreak_andAdvertIsNotPlaying_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_NOT_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);

            long positionInAdvertBreakInMillis = facade.positionInAdvertBreakInMillis();

            assertThat(positionInAdvertBreakInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingPositionInAdvertBreak_andPlayerIsNotReady_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_NOT_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(true);

            long positionInAdvertBreakInMillis = facade.positionInAdvertBreakInMillis();

            assertThat(positionInAdvertBreakInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingPositionInAdvertBreak_andAdvertLoaderIsMissing_thenReturnsZero() {
            given(exoPlayer.isPlayingAd()).willReturn(IS_PLAYING);
            given(exoPlayer.getPlayWhenReady()).willReturn(IS_NOT_PLAYING);
            given(optionalAdsLoader.isPresent()).willReturn(false);

            long positionInAdvertBreakInMillis = facade.positionInAdvertBreakInMillis();

            assertThat(positionInAdvertBreakInMillis).isEqualTo(0);
        }

        @Test
        public void whenGettingBufferPercentage_thenReturnsBufferPercentage() {
            given(exoPlayer.getBufferedPercentage()).willReturn(TEN_PERCENT);

            int bufferPercentage = facade.bufferPercentage();

            assertThat(bufferPercentage).isEqualTo(TEN_PERCENT);
        }

        @Test
        public void whenSelectingAudioTrack_thenDelegatesToTrackSelector() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

            facade.selectAudioTrack(audioTrack);

            verify(trackSelector).selectAudioTrack(audioTrack, rendererTypeRequester);
        }

        @Test
        public void givenSelectingAudioTrackSuceeds_whenSelectingAudioTrack_thenReturnsTrue() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);
            given(trackSelector.selectAudioTrack(audioTrack, rendererTypeRequester)).willReturn(true);

            boolean success = facade.selectAudioTrack(audioTrack);

            assertThat(success).isTrue();
        }

        @Test
        public void givenSelectingAudioTrackFails_whenSelectingAudioTrack_thenReturnsFalse() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);
            given(trackSelector.selectAudioTrack(audioTrack, rendererTypeRequester)).willReturn(false);

            boolean success = facade.selectAudioTrack(audioTrack);

            assertThat(success).isFalse();
        }

        @Test
        public void whenSelectingSubtitlesTrack_thenDelegatesToTrackSelector() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);

            facade.selectSubtitleTrack(subtitleTrack);

            verify(trackSelector).selectTextTrack(subtitleTrack, rendererTypeRequester);
        }

        @Test
        public void givenSelectingTextTrackSuceeds_whenSelectingSubtitlesTrack_thenReturnsTrue() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);
            given(trackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester)).willReturn(true);

            boolean success = facade.selectSubtitleTrack(subtitleTrack);

            assertThat(success).isTrue();
        }

        @Test
        public void givenSelectingTextTrackFails_whenSelectingSubtitlesTrack_thenReturnsFalse() {
            PlayerSubtitleTrack subtitleTrack = mock(PlayerSubtitleTrack.class);
            given(trackSelector.selectTextTrack(subtitleTrack, rendererTypeRequester)).willReturn(false);

            boolean success = facade.selectSubtitleTrack(subtitleTrack);

            assertThat(success).isFalse();
        }

        @Test
        public void whenGettingAudioTracks_thenDelegatesToTrackSelector() {
            given(trackSelector.getAudioTracks(any(RendererTypeRequester.class))).willReturn(AUDIO_TRACKS);

            AudioTracks audioTracks = facade.getAudioTracks();

            assertThat(audioTracks).isEqualTo(AUDIO_TRACKS);
        }

        @Test
        public void whenGettingSelectedVideoTrack_thenDelegatesTrackSelector() {
            given(trackSelector.getSelectedVideoTrack(eq(exoPlayer), any(RendererTypeRequester.class), any(ContentType.class))).willReturn(Optional.of(PLAYER_VIDEO_TRACK));

            Optional<PlayerVideoTrack> selectedVideoTrack = facade.getSelectedVideoTrack();

            assertThat(selectedVideoTrack).isEqualTo(Optional.of(PLAYER_VIDEO_TRACK));
        }

        @Test
        public void whenSelectingVideoTrack_thenDelegatesToTrackSelector() {
            given(trackSelector.selectVideoTrack(eq(PLAYER_VIDEO_TRACK), any(RendererTypeRequester.class))).willReturn(SELECTED);

            boolean selectedVideoTrack = facade.selectVideoTrack(PLAYER_VIDEO_TRACK);

            assertThat(selectedVideoTrack).isTrue();
        }

        @Test
        public void whenGettingVideoTracks_thenDelegatesToTrackSelector() {
            given(trackSelector.getVideoTracks(any(RendererTypeRequester.class), any(ContentType.class))).willReturn(VIDEO_TRACKS);

            List<PlayerVideoTrack> videoTracks = facade.getVideoTracks();

            assertThat(videoTracks).isEqualTo(VIDEO_TRACKS);
        }

        @Test
        public void whenSetRepeatingTrue_thenSetsRepeatModeAll() {
            facade.setRepeating(true);

            verify(exoPlayer).setRepeatMode(Player.REPEAT_MODE_ALL);
        }

        @Test
        public void whenSetRepeatingFalse_thenSetsRepeatModeOff() {
            facade.setRepeating(false);

            verify(exoPlayer).setRepeatMode(Player.REPEAT_MODE_OFF);
        }

        @Test
        public void whenSetVolume_thenSetsPlayerVolume() {
            facade.setVolume(ANY_VOLUME);

            verify(exoPlayer).setVolume(ANY_VOLUME);
        }

        @Test
        public void whenGetVolume_thenGetsPlayerVolume() {
            given(exoPlayer.getVolume()).willReturn(ANY_VOLUME);

            float currentVolume = facade.getVolume();

            assertThat(currentVolume).isEqualTo(ANY_VOLUME);
        }

    }

    public abstract static class Base {

        static final float ANY_VOLUME = 0.5f;
        static final boolean allowFallbackDecoder = true;
        static final boolean requiresSecureDecoder = true;

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        BandwidthMeterCreator bandwidthMeterCreator;
        @Mock
        DefaultBandwidthMeter defaultBandwidthMeter;
        @Mock
        AndroidDeviceVersion androidDeviceVersion;
        @Mock
        SimpleExoPlayer exoPlayer;
        @Mock
        MediaSourceFactory mediaSourceFactory;
        @Mock
        ExoPlayerForwarder exoPlayerForwarder;
        @Mock
        CompositeTrackSelectorCreator trackSelectorCreator;
        @Mock
        CompositeTrackSelector trackSelector;
        @Mock
        Uri uri;
        @Mock
        RendererTypeRequester rendererTypeRequester;
        @Mock
        RendererTypeRequesterCreator rendererTypeRequesterCreator;
        @Mock
        Optional<NoPlayerAdsLoader> optionalAdsLoader;
        @Mock
        NoPlayerAdsLoader adsLoader;
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        DefaultDrmSessionEventListener drmSessionEventListener;
        @Mock
        MediaSourceEventListener mediaSourceEventListener;
        @Mock
        Optional<NoPlayer.AdvertListener> optionalAdvertListener;
        @Mock
        NoPlayer.AdvertListener advertListener;
        @Mock
        SurfaceView surfaceView;
        @Mock
        TextureView textureView;
        @Mock
        ExoPlayerCreator exoPlayerCreator;
        @Mock
        Timeline timeline;
        PlayerSurfaceHolder surfaceViewHolder;
        PlayerSurfaceHolder textureViewHolder;

        ExoPlayerFacade facade;

        @Before
        public void setUp() {
            given(exoPlayerForwarder.drmSessionEventListener()).willReturn(drmSessionEventListener);
            given(exoPlayerForwarder.mediaSourceEventListener()).willReturn(mediaSourceEventListener);
            given(exoPlayerForwarder.advertListener()).willReturn(optionalAdvertListener);
            given(bandwidthMeterCreator.create(anyLong())).willReturn(defaultBandwidthMeter);
            given(trackSelectorCreator.create(any(Options.class), eq(defaultBandwidthMeter))).willReturn(trackSelector);
            given(exoPlayerCreator.create(drmSessionCreator, drmSessionEventListener, allowFallbackDecoder, requiresSecureDecoder, OPTIONS, trackSelector.trackSelector())).willReturn(exoPlayer);
            willDoNothing().given(exoPlayer).seekTo(anyInt());
            given(rendererTypeRequesterCreator.createfrom(exoPlayer)).willReturn(rendererTypeRequester);
            given(exoPlayer.getCurrentTimeline()).willReturn(timeline);
            facade = new ExoPlayerFacade(
                    bandwidthMeterCreator,
                    androidDeviceVersion,
                    mediaSourceFactory,
                    trackSelectorCreator,
                    exoPlayerCreator,
                    rendererTypeRequesterCreator,
                    optionalAdsLoader
            );
            given(surfaceView.getHolder()).willReturn(mock(SurfaceHolder.class));
            surfaceViewHolder = PlayerSurfaceHolder.create(surfaceView);
            textureViewHolder = PlayerSurfaceHolder.create(textureView);
        }

        MediaSource givenMediaSource(Options options) {
            MediaSource mediaSource = mock(MediaSource.class);
            given(
                    mediaSourceFactory.create(
                            options,
                            uri,
                            mediaSourceEventListener,
                            defaultBandwidthMeter,
                            optionalAdsLoader
                    )
            ).willReturn(mediaSource);

            return mediaSource;
        }

        void givenAdGroupAtPositionContainsAdsWithDurations(int position, long[] durations) {
            Timeline contentTimeline = new SinglePeriodTimeline(1, false, false);
            SinglePeriodAdTimeline timeline = new SinglePeriodAdTimeline(contentTimeline, adPlaybackState(position, durations));
            given(exoPlayer.getCurrentTimeline()).willReturn(timeline);
            given(exoPlayer.getCurrentAdGroupIndex()).willReturn(position);
        }

        AdPlaybackState adPlaybackState(int position, long[] durations) {
            long[] adGroupTimesUs = {0, 100, 200, 300};
            long[][] adGroupDurations = {
                    ADVERT_DURATIONS,
                    ADVERT_DURATIONS,
                    ADVERT_DURATIONS,
                    ADVERT_DURATIONS
            };
            adGroupDurations[position] = durations;
            return new AdPlaybackState(adGroupTimesUs)
                    .withAdCount(0, adGroupDurations[0].length)
                    .withAdCount(1, adGroupDurations[1].length)
                    .withAdCount(2, adGroupDurations[2].length)
                    .withAdCount(3, adGroupDurations[3].length)
                    .withAdDurationsUs(adGroupDurations);
        }
    }
}
