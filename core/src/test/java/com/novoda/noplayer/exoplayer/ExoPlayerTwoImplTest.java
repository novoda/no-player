package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.Player.StateChangedListener;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.listeners.BitrateChangedListeners;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.noplayer.player.PlayerInformation;
import com.novoda.noplayer.player.PlayerType;

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
    private static final Player.LoadTimeoutCallback ANY_LOAD_TIMEOUT_CALLBACK = new Player.LoadTimeoutCallback() {
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

            verify(forwarder).bind(preparedListeners, player);
            verify(forwarder).bind(completionListeners, stateChangedListeners);
            verify(forwarder).bind(errorListeners, player);
            verify(forwarder).bind(bufferStateListeners);
            verify(forwarder).bind(videoSizeChangedListeners);
            verify(forwarder).bind(bitrateChangedListeners);
            verify(forwarder).bind(infoListeners);
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

            ArgumentCaptor<Player.PreparedListener> argumentCaptor = ArgumentCaptor.forClass(Player.PreparedListener.class);

            verify(listenersHolder).addPreparedListener(argumentCaptor.capture());
            Player.PreparedListener preparedListener = argumentCaptor.getValue();
            preparedListener.onPrepared(player);

            verify(loadTimeout).cancel();
        }

        @Test
        public void givenPlayerIsInitialised_whenVideoHasError_thenCancelsTimeout() {
            player.initialise();

            ArgumentCaptor<Player.ErrorListener> argumentCaptor = ArgumentCaptor.forClass(Player.ErrorListener.class);

            verify(listenersHolder).addErrorListener(argumentCaptor.capture());
            Player.ErrorListener errorListener = argumentCaptor.getValue();
            errorListener.onError(player, mock(Player.PlayerError.class));

            verify(loadTimeout).cancel();
        }

        @Test
        public void givenPlayerIsInitialised_andPlayerViewIsAttached_whenVideoSizeChanges_thenPlayerVideoWidthAndHeightMatches() {
            player.initialise();
            player.attach(playerView);

            ArgumentCaptor<Player.VideoSizeChangedListener> argumentCaptor = ArgumentCaptor.forClass(Player.VideoSizeChangedListener.class);
            verify(listenersHolder, times(2)).addVideoSizeChangedListener(argumentCaptor.capture());

            Player.VideoSizeChangedListener videoSizeChangedListener = argumentCaptor.getAllValues().get(INDEX_INTERNAL_VIDEO_SIZE_CHANGED_LISTENER);
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

            ArgumentCaptor<Player.VideoSizeChangedListener> argumentCaptor = ArgumentCaptor.forClass(Player.VideoSizeChangedListener.class);
            verify(listenersHolder, times(2)).addVideoSizeChangedListener(argumentCaptor.capture());
            Player.VideoSizeChangedListener videoSizeChangedListener = argumentCaptor.getAllValues().get(1);
            assertThat(videoSizeChangedListener).isSameAs(playerView.getVideoSizeChangedListener());
        }

        @Test
        public void whenReleasing_thenPlayerResourcesAreReleased() {

            player.release();

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder).clear();
        }

        @Test
        public void whenLoadingVideo_thenDelegatesLoadingToFacade() {

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(exoPlayerFacade).loadVideo(drmSessionCreator, uri, ANY_CONTENT_TYPE, forwarder);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenDelegatesLoadingToFacade() {

            player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(exoPlayerFacade).loadVideo(drmSessionCreator, uri, ANY_CONTENT_TYPE, forwarder);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenStartsLoadTimeout() {

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
        public void whenLoadingVideo_thenAddsStateChangedListenerToListenersHolder() {
            player.attach(playerView);

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(listenersHolder).addStateChangedListener(playerView.getStateChangedListener());
        }

        @Test
        public void whenLoadingVideo_thenAddsVideoSizeChangedListenerToListenersHolder() {
            player.attach(playerView);

            player.loadVideo(uri, ANY_CONTENT_TYPE);

            verify(listenersHolder).addVideoSizeChangedListener(playerView.getVideoSizeChangedListener());
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
        public void whenDetachingPlayerView_thenRemovesVideoSizeChangedListener() {

            player.detach(playerView);

            verify(listenersHolder).removeVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenDetachingPlayerView_thenRemovesStateChangedListener() {

            player.detach(playerView);

            verify(listenersHolder).removeStateChangedListener(stateChangeListener);
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideo_thenPlayerIsReleased_andNotListeners() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(true);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreReleased_andNotListeners() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(true);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideo_thenPlayerResourcesAreNotReleased() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(false);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListeners, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(exoPlayerFacade, never()).release();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreNotReleased() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(false);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListeners, never()).onVideoStopped();
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

            verify(stateChangedListeners).onVideoPaused();
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

            verify(exoPlayerFacade).play(surfaceHolder);
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

            verify(stateChangedListeners).onVideoPlaying();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenersThatVideoIsPlaying() {

            player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

            verify(stateChangedListeners).onVideoPlaying();
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
        Player.VideoSizeChangedListener videoSizeChangedListener;
        @Mock
        PlayerListenersHolder listenersHolder;
        @Mock
        ErrorListeners errorListeners;
        @Mock
        PreparedListeners preparedListeners;
        @Mock
        BufferStateListeners bufferStateListeners;
        @Mock
        CompletionListeners completionListeners;
        @Mock
        StateChangedListeners stateChangedListeners;
        @Mock
        InfoListeners infoListeners;
        @Mock
        VideoSizeChangedListeners videoSizeChangedListeners;
        @Mock
        BitrateChangedListeners bitrateChangedListeners;
        @Mock
        DrmSessionCreator drmSessionCreator;
        @Mock
        ExoPlayerFacade exoPlayerFacade;

        ExoPlayerTwoImpl player;

        @Before
        public void setUp() {
            given(playerView.getSurfaceHolderRequester()).willReturn(surfaceHolderRequester);
            given(playerView.getStateChangedListener()).willReturn(stateChangeListener);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    SurfaceHolderRequester.Callback callback = invocation.getArgument(0);
                    callback.onSurfaceHolderReady(surfaceHolder);
                    return null;
                }
            }).when(surfaceHolderRequester).requestSurfaceHolder(any(SurfaceHolderRequester.Callback.class));

            given(listenersHolder.getErrorListeners()).willReturn(errorListeners);
            given(listenersHolder.getPreparedListeners()).willReturn(preparedListeners);
            given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListeners);
            given(listenersHolder.getCompletionListeners()).willReturn(completionListeners);
            given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListeners);
            given(listenersHolder.getInfoListeners()).willReturn(infoListeners);
            given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListeners);
            given(listenersHolder.getBitrateChangedListeners()).willReturn(bitrateChangedListeners);

            player = new ExoPlayerTwoImpl(
                    exoPlayerFacade,
                    listenersHolder,
                    forwarder,
                    loadTimeout,
                    heart,
                    drmSessionCreator
            );
        }
    }
}
