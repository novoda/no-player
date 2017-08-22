package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.text.Cue;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayer.StateChangedListener;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerType;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.TextCues;
import com.novoda.noplayer.model.Timeout;
import com.novoda.noplayer.model.VideoPosition;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static android.provider.CalendarContract.CalendarCache.URI;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class ExoPlayerTwoImplTest {

    private static final long TWO_MINUTES_IN_MILLIS = 120000;
    private static final long TEN_SECONDS = 10;

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int ANY_ROTATION_DEGREES = 30;
    private static final int ANY_PIXEL_WIDTH_HEIGHT = 75;

    private static final boolean IS_BEATING = true;
    private static final boolean IS_NOT_BEATING = false;

    private static final ContentType ANY_CONTENT_TYPE = ContentType.DASH;
    private static final Timeout ANY_TIMEOUT = Timeout.fromSeconds(TEN_SECONDS);
    private static final NoPlayer.LoadTimeoutCallback ANY_LOAD_TIMEOUT_CALLBACK = new NoPlayer.LoadTimeoutCallback() {
        @Override
        public void onLoadTimeout() {

        }
    };
    private static final int INDEX_INTERNAL_VIDEO_SIZE_CHANGED_LISTENER = 0;

    public static class GivenVideoNotLoaded extends Base {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void whenInitialisingPlayer_thenBindsListenersToForwarder() {
            player.initialise();

            verify(forwarder).bind(preparedListener, player);
            verify(forwarder).bind(completionListener, stateChangedListener);
            verify(forwarder).bind(errorListener);
            verify(forwarder).bind(bufferStateListener);
            verify(forwarder).bind(videoSizeChangedListener);
            verify(forwarder).bind(bitrateChangedListener);
            verify(forwarder).bind(infoListener);
        }

        @Test
        public void whenInitialisingPlayer_thenBindsHeart() {
            player.initialise();

            verify(listenersHolder).getHeartbeatCallbacks();
            verify(heart).bind(any(Heart.Heartbeat.class));
        }

        @Test
        public void givenPlayerIsInitialised_whenVideoIsPrepared_thenCancelsTimeout() {
            player.initialise();

            ArgumentCaptor<NoPlayer.PreparedListener> argumentCaptor = ArgumentCaptor.forClass(NoPlayer.PreparedListener.class);

            verify(listenersHolder).addPreparedListener(argumentCaptor.capture());
            NoPlayer.PreparedListener preparedListener = argumentCaptor.getValue();
            preparedListener.onPrepared(player);

            verify(loadTimeout).cancel();
        }

        @Test
        public void givenPlayerIsInitialised_whenVideoHasError_thenPlayerResourcesAreReleased_andNotListeners() {
            player.initialise();

            ArgumentCaptor<NoPlayer.ErrorListener> argumentCaptor = ArgumentCaptor.forClass(NoPlayer.ErrorListener.class);

            verify(listenersHolder).addErrorListener(argumentCaptor.capture());
            NoPlayer.ErrorListener errorListener = argumentCaptor.getValue();
            errorListener.onError(mock(NoPlayer.PlayerError.class));

            verify(listenersHolder).resetPreparedState();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder, never()).clear();
            verify(stateChangedListener, never()).onVideoStopped();
        }

        @Test
        public void givenPlayerIsInitialised_andPlayerViewIsAttached_whenVideoSizeChanges_thenPlayerVideoWidthAndHeightMatches() {
            player.initialise();
            player.attach(playerView);

            ArgumentCaptor<NoPlayer.VideoSizeChangedListener> argumentCaptor = ArgumentCaptor.forClass(NoPlayer.VideoSizeChangedListener.class);
            verify(listenersHolder, times(2)).addVideoSizeChangedListener(argumentCaptor.capture());

            NoPlayer.VideoSizeChangedListener videoSizeChangedListener = argumentCaptor.getAllValues().get(INDEX_INTERNAL_VIDEO_SIZE_CHANGED_LISTENER);
            videoSizeChangedListener.onVideoSizeChanged(WIDTH, HEIGHT, ANY_ROTATION_DEGREES, ANY_PIXEL_WIDTH_HEIGHT);

            int actualWidth = player.getVideoWidth();
            int actualHeight = player.getVideoHeight();

            assertThat(actualWidth).isEqualTo(WIDTH);
            assertThat(actualHeight).isEqualTo(HEIGHT);
        }

        @Test
        public void givenPlayerIsInitialised_whenAttachingPlayerView_thenAddsPlayerViewVideoSizeChangedListenerToListenersHolder() {
            player.initialise();

            player.attach(playerView);

            ArgumentCaptor<NoPlayer.VideoSizeChangedListener> argumentCaptor = ArgumentCaptor.forClass(NoPlayer.VideoSizeChangedListener.class);
            verify(listenersHolder, times(2)).addVideoSizeChangedListener(argumentCaptor.capture());
            NoPlayer.VideoSizeChangedListener videoSizeChangedListener = argumentCaptor.getAllValues().get(1);
            assertThat(videoSizeChangedListener).isSameAs(playerView.getVideoSizeChangedListener());
        }

        @Test
        public void whenStopping_thenPlayerResourcesAreReleased() {

            player.stop();

            verify(listenersHolder).resetPreparedState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
        }

        @Test
        public void whenReleasing_thenPlayerResourcesAreReleased() {

            player.release();

            verify(listenersHolder).resetPreparedState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder).clear();
        }

        @Test
        public void givenAttachedPlayerView_whenStopping_thenPlayerResourcesAreReleased() {
            player.attach(playerView);

            player.stop();

            verify(listenersHolder).resetPreparedState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(containerView).setVisibility(View.GONE);
            verify(exoPlayerFacade).release();
        }

        @Test
        public void givenAttachedPlayerView_whenReleasing_thenPlayerResourcesAreReleased() {
            player.attach(playerView);

            player.release();

            verify(listenersHolder).resetPreparedState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(containerView).setVisibility(View.GONE);
            verify(exoPlayerFacade).release();
            verify(listenersHolder).clear();
        }

        @Test
        public void whenLoadingVideo_thenDelegatesLoadingToFacade() {
            player.attach(playerView);

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(exoPlayerFacade).loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, forwarder, mediaCodecSelector);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenDelegatesLoadingToFacade() {
            player.attach(playerView);

            player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(exoPlayerFacade).loadVideo(surfaceHolder, drmSessionCreator, uri, ANY_CONTENT_TYPE, forwarder, mediaCodecSelector);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenStartsLoadTimeout() {
            player.attach(playerView);

            player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(loadTimeout).start(ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);
        }

        @Test
        public void whenGettingPlayerInformation_thenReturnsPlayerInformation() {
            PlayerInformation playerInformation = player.getPlayerInformation();

            assertThat(playerInformation.getPlayerType()).isEqualTo(PlayerType.EXO_PLAYER);
            assertThat(playerInformation.getVersion()).isEqualTo(ExoPlayerLibraryInfo.VERSION);
        }

        @Test
        public void whenQueryingIsPlaying_thenReturnsFalse() {

            boolean isPlaying = player.isPlaying();

            assertThat(isPlaying).isFalse();
        }

        @Test
        public void whenAttachingPlayerView_thenAddsVideoSizeChangedListener() {

            player.attach(playerView);

            verify(listenersHolder).addVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenAttachingPlayerView_thenAddsStateChangedListener() {

            player.attach(playerView);

            verify(listenersHolder).addStateChangedListener(stateChangeListener);
        }

        @Test
        public void givenAttachedPlayerView_whenDetachingPlayerView_thenRemovesVideoSizeChangedListener() {
            player.attach(playerView);

            player.detach(playerView);

            verify(listenersHolder).removeVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void givenAttachedPlayerView_whenDetachingPlayerView_thenRemovesStateChangedListener() {
            player.attach(playerView);

            player.detach(playerView);

            verify(listenersHolder).removeStateChangedListener(stateChangeListener);
        }

        @Test
        public void givenAttachedPlayerView_whenLoadingVideo_thenMakesContainerVisible() {
            player.attach(playerView);

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(containerView).setVisibility(View.VISIBLE);
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideo_thenPlayerIsReleased_andNotListeners() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(true);
            player.attach(playerView);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreReleased_andNotListeners() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(true);
            player.attach(playerView);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideo_thenPlayerResourcesAreNotReleased() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(false);
            player.attach(playerView);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(exoPlayerFacade, never()).release();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreNotReleased() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(false);
            player.attach(playerView);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(exoPlayerFacade, never()).release();
        }
    }

    public static class GivenAttachedAndVideoIsLoaded extends Base {

        @Override
        public void setUp() {
            super.setUp();
            player.attach(playerView);
            player.loadVideo(uri, ANY_CONTENT_TYPE);
        }

        @Test
        public void whenLoadingVideo_thenAddsStateChangedListenerToListenersHolder() {

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(listenersHolder).addStateChangedListener(playerView.getStateChangedListener());
        }

        @Test
        public void whenLoadingVideo_thenAddsVideoSizeChangedListenerToListenersHolder() {

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(listenersHolder).addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
        }

        @Test
        public void whenReleasing_thenResetsFacade() {
            player.release();

            verify(exoPlayerFacade).release();
        }

        @Test
        public void whenStartingPlayback_thenStartsBeatingHeart() {

            player.play();

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenPausing_thenNotifiesStateListenersThatVideoIsPaused() {
            player.pause();

            verify(stateChangedListener).onVideoPaused();
        }

        @Test
        public void givenHeartIsBeating_whenPausing_thenStopsBeatingHeart() {
            given(heart.isBeating()).willReturn(IS_BEATING);

            player.pause();

            verify(heart).stopBeatingHeart();
        }

        @Test
        public void givenHeartIsBeating_whenPausing_thenForcesHeartBeat() {
            given(heart.isBeating()).willReturn(IS_BEATING);

            player.pause();

            verify(heart).forceBeat();
        }

        @Test
        public void givenHeartIsNotBeating_whenPausing_thenDoesNotStopBeatingHeart() {
            given(heart.isBeating()).willReturn(IS_NOT_BEATING);

            player.pause();

            verify(heart, never()).stopBeatingHeart();
        }

        @Test
        public void givenHeartIsNotBeating_whenPausing_thenDoesNotForceHeartBeat() {
            given(heart.isBeating()).willReturn(IS_NOT_BEATING);

            player.pause();

            verify(heart, never()).forceBeat();
        }

        @Test
        public void whenSeeking_thenSeeksToPosition() {
            VideoPosition videoPosition = VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS);

            player.seekTo(videoPosition);

            verify(exoPlayerFacade).seekTo(videoPosition);
        }

        @Test
        public void whenStartingPlayback_andSurfaceHolderIsReady_thenPlaysFacadeWithSurfaceHolder() {
            // TODO replace answer with captor

            player.play();

            verify(exoPlayerFacade).play();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSeeksToPosition() {

            player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(exoPlayerFacade).seekTo(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenStartsBeatingHeart() {

            player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlay_thenNotifiesStateListenersThatVideoIsPlaying() {

            player.play();

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenersThatVideoIsPlaying() {

            player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void whenSelectingSubtitlesTrack_thenShowsPlayerSubtitlesView() {
            PlayerSubtitleTrack playerSubtitleTrack = PlayerSubtitleTrackFixture.anInstance().build();

            player.showSubtitleTrack(playerSubtitleTrack);

            verify(playerView).showSubtitles();
        }

        @Test
        public void givenSelectingSubtitleTrackSuceeds_whenSelectingSubtitlesTrack_thenReturnsTrue() {
            PlayerSubtitleTrack playerSubtitleTrack = mock(PlayerSubtitleTrack.class);
            given(exoPlayerFacade.selectSubtitleTrack(playerSubtitleTrack)).willReturn(true);

            boolean success = player.showSubtitleTrack(playerSubtitleTrack);

            assertThat(success).isTrue();
        }

        @Test
        public void givenSelectingSubtitleTrackFails_whenSelectingSubtitlesTrack_thenReturnsFalse() {
            PlayerSubtitleTrack playerSubtitleTrack = mock(PlayerSubtitleTrack.class);
            given(exoPlayerFacade.selectSubtitleTrack(playerSubtitleTrack)).willReturn(false);

            boolean success = player.showSubtitleTrack(playerSubtitleTrack);

            assertThat(success).isFalse();
        }

        @Test
        public void givenPlayerHasLoadedSubtitleCues_whenSelectingSubtitlesTrack_thenSetsSubtitleCuesOnView() {
            TextCues textCues = givenPlayerHasLoadedSubtitleCues();

            PlayerSubtitleTrack playerSubtitleTrack = PlayerSubtitleTrackFixture.anInstance().build();

            player.showSubtitleTrack(playerSubtitleTrack);

            verify(playerView).setSubtitleCue(textCues);
        }

        private TextCues givenPlayerHasLoadedSubtitleCues() {
            final List<Cue> cueList = Arrays.asList(new Cue("first cue"), new Cue("secondCue"));
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    TextRendererOutput output = invocation.getArgument(0);
                    output.output().onCues(cueList);
                    return null;
                }
            }).when(exoPlayerFacade).setSubtitleRendererOutput(any(TextRendererOutput.class));
            return ExoPlayerCueMapper.map(cueList);
        }

        @Test
        public void whenClearingSubtitles_thenHidesPlayerSubtitlesView() {
            player.hideSubtitleTrack();

            verify(playerView).hideSubtitles();
        }
    }

    public abstract static class Base {

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        ExoPlayerForwarder forwarder;
        @Mock
        LoadTimeout loadTimeout;
        @Mock
        Heart heart;
        @Mock
        Uri uri;
        @Mock
        PlayerView playerView;
        @Mock
        SurfaceHolderRequester surfaceHolderRequester;
        @Mock
        SurfaceHolder surfaceHolder;
        @Mock
        StateChangedListener stateChangeListener;
        @Mock
        NoPlayer.VideoSizeChangedListener videoSizeChangedListener;
        @Mock
        PlayerListenersHolder listenersHolder;
        @Mock
        NoPlayer.ErrorListener errorListener;
        @Mock
        NoPlayer.PreparedListener preparedListener;
        @Mock
        NoPlayer.BufferStateListener bufferStateListener;
        @Mock
        NoPlayer.CompletionListener completionListener;
        @Mock
        NoPlayer.StateChangedListener stateChangedListener;
        @Mock
        NoPlayer.InfoListener infoListener;
        @Mock
        NoPlayer.BitrateChangedListener bitrateChangedListener;
        @Mock
        ExoPlayerFacade exoPlayerFacade;
        @Mock
        ExoPlayerTrackSelector exoPlayerTrackSelector;
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        MediaCodecSelector mediaCodecSelector;
        @Mock
        View containerView;

        ExoPlayerTwoImpl player;

        @Before
        public void setUp() {
            given(playerView.getSurfaceHolderRequester()).willReturn(surfaceHolderRequester);
            given(playerView.getStateChangedListener()).willReturn(stateChangeListener);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            given(playerView.getContainerView()).willReturn(containerView);
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    SurfaceHolderRequester.Callback callback = invocation.getArgument(0);
                    callback.onSurfaceHolderReady(surfaceHolder);
                    return null;
                }
            }).when(surfaceHolderRequester).requestSurfaceHolder(any(SurfaceHolderRequester.Callback.class));

            given(listenersHolder.getErrorListeners()).willReturn(errorListener);
            given(listenersHolder.getPreparedListeners()).willReturn(preparedListener);
            given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListener);
            given(listenersHolder.getCompletionListeners()).willReturn(completionListener);
            given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListener);
            given(listenersHolder.getInfoListeners()).willReturn(infoListener);
            given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListener);
            given(listenersHolder.getBitrateChangedListeners()).willReturn(bitrateChangedListener);

            player = new ExoPlayerTwoImpl(
                    exoPlayerFacade,
                    listenersHolder,
                    forwarder,
                    loadTimeout,
                    heart,
                    drmSessionCreator,
                    mediaCodecSelector
            );
        }
    }
}
