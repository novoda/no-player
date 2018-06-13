package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceRequester;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.internal.utils.NoPlayerLog;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.Either;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerAudioTrackFixture;
import com.novoda.noplayer.model.Timeout;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class AndroidMediaPlayerImplTest {

    public static class GivenPlayer extends Base {

        private static final boolean IS_BEATING = true;
        private static final boolean IS_NOT_BEATING = false;
        private static final int WIDTH = 100;
        private static final int HEIGHT = 200;
        private static final int ANY_ROTATION_DEGREES = 0;
        private static final int ANY_PIXEL_WIDTH_HEIGHT = 1;
        private static final long TWO_MINUTES_IN_MILLIS = 120000;
        private static final int ONE_SECOND_IN_MILLIS = 1000;
        private static final boolean IS_PLAYING = true;
        private static final PlayerAudioTrack PLAYER_AUDIO_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().build();
        private static final AudioTracks AUDIO_TRACKS = AudioTracks.from(Collections.singletonList(PLAYER_AUDIO_TRACK));

        @Test
        public void whenInitialising_thenBindsListenersToForwarder() {
            player.initialise();

            verify(forwarder).bind(preparedListener, player);
            verify(forwarder).bind(bufferStateListener, errorListener);
            verify(forwarder).bind(completionListener, stateChangedListener);
            verify(forwarder).bind(videoSizeChangedListener);
            verify(forwarder).bind(infoListener);
        }

        @Test
        public void whenInitialising_thenBindsListenerToBufferHeartbeatCallback() {
            player.initialise();

            verify(checkBufferHeartbeatCallback).bind(bufferListener);
        }

        @Test
        public void whenInitialising_thenBindsHeartbeatCallbackToListenerHolder() {
            player.initialise();

            verify(listenersHolder).addHeartbeatCallback(checkBufferHeartbeatCallback);
        }

        @Test
        public void givenInitialised_whenCallingOnPrepared_thenCancelsTimeout() {
            player.initialise();

            ArgumentCaptor<NoPlayer.PreparedListener> preparedListenerCaptor = ArgumentCaptor.forClass(NoPlayer.PreparedListener.class);
            verify(listenersHolder).addPreparedListener(preparedListenerCaptor.capture());

            NoPlayer.PreparedListener preparedListener = preparedListenerCaptor.getValue();
            preparedListener.onPrepared(player);

            verify(loadTimeout).cancel();
        }

        @Test
        public void whenInitialising_thenBindsHeart() {
            player.initialise();

            verify(heart).bind(any(Heart.Heartbeat.class));
        }

        @Test
        public void givenInitialised_whenCallingOnPrepared_thenSetsOnSeekCompleteListener() {
            player.initialise();
            ArgumentCaptor<NoPlayer.PreparedListener> preparedListenerCaptor = ArgumentCaptor.forClass(NoPlayer.PreparedListener.class);
            verify(listenersHolder).addPreparedListener(preparedListenerCaptor.capture());

            NoPlayer.PreparedListener preparedListener = preparedListenerCaptor.getValue();
            preparedListener.onPrepared(player);

            verify(mediaPlayer).setOnSeekCompleteListener(any(MediaPlayer.OnSeekCompleteListener.class));
        }

        @Test
        public void givenInitialised_whenCallingOnError_thenCancelsTimeout() {
            player.initialise();
            ArgumentCaptor<NoPlayer.ErrorListener> errorListenerCaptor = ArgumentCaptor.forClass(NoPlayer.ErrorListener.class);
            verify(listenersHolder).addErrorListener(errorListenerCaptor.capture());

            NoPlayer.ErrorListener errorListener = errorListenerCaptor.getValue();
            errorListener.onError(mock(NoPlayer.PlayerError.class));

            verify(loadTimeout).cancel();
        }

        @Test
        public void givenInitialised_whenCallingOnError_thenPlayerResourcesAreReleased_andNotListeners() {
            player.initialise();
            ArgumentCaptor<NoPlayer.ErrorListener> errorListenerCaptor = ArgumentCaptor.forClass(NoPlayer.ErrorListener.class);
            verify(listenersHolder).addErrorListener(errorListenerCaptor.capture());

            NoPlayer.ErrorListener errorListener = errorListenerCaptor.getValue();
            errorListener.onError(mock(NoPlayer.PlayerError.class));

            verify(delayedActionExecutor).clearAllActions();
            verify(listenersHolder).resetState();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
            verify(stateChangedListener, never()).onVideoStopped();
        }

        @Test
        public void givenInitialised_whenCallingOnVideoSizeChanged_thenVideoWidthAndHeightMatches() {
            player.initialise();
            ArgumentCaptor<NoPlayer.VideoSizeChangedListener> videoSizeChangedListenerCaptor = ArgumentCaptor.forClass(NoPlayer.VideoSizeChangedListener.class);
            verify(listenersHolder).addVideoSizeChangedListener(videoSizeChangedListenerCaptor.capture());

            NoPlayer.VideoSizeChangedListener videoSizeChangedListener = videoSizeChangedListenerCaptor.getValue();
            videoSizeChangedListener.onVideoSizeChanged(WIDTH, HEIGHT, ANY_ROTATION_DEGREES, ANY_PIXEL_WIDTH_HEIGHT);

            assertThat(player.videoWidth()).isEqualTo(WIDTH);
            assertThat(player.videoHeight()).isEqualTo(HEIGHT);
        }

        @Test
        public void givenAndroidMediaPlayerIsPlaying_whenCallingIsPlaying_thenReturnsTrue() {
            given(mediaPlayer.isPlaying()).willReturn(IS_PLAYING);

            boolean isPlaying = player.isPlaying();

            assertThat(isPlaying).isTrue();
        }

        @Test
        public void whenSeeking_thenSeeksToPosition() {
            long seekPositionInMillis = TWO_MINUTES_IN_MILLIS;

            player.seekTo(seekPositionInMillis);

            verify(mediaPlayer).seekTo(seekPositionInMillis);
        }

        @Test
        public void whenPausing_thenPausesMediaPlayer() {
            player.pause();

            verify(mediaPlayer).pause();
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
        public void whenPausing_thenNotifiesStateChangedListenersThatVideoIsPaused() {
            player.pause();

            verify(stateChangedListener).onVideoPaused();
        }

        @Test
        public void givenPlayerIsNotSeeking_whenGettingPlayheadPosition_thenReturnsCurrentMediaPlayerPosition() {
            given(mediaPlayer.currentPositionInMillis()).willReturn(ONE_SECOND_IN_MILLIS);
            long playheadPositionInMillis = player.playheadPositionInMillis();

            assertThat(playheadPositionInMillis).isEqualTo(ONE_SECOND_IN_MILLIS);
        }

        @Test
        public void givenPlayerIsSeeking_whenGettingPlayheadPosition_thenReturnsSeekPosition() {
            long seekPositionInMillis = TEN_SECONDS;
            player.seekTo(seekPositionInMillis);

            long videoPositionInMillis = player.playheadPositionInMillis();

            assertThat(videoPositionInMillis).isEqualTo(seekPositionInMillis);
        }

        @Test
        public void whenGettingMediaDuration_thenReturnsMediaPlayerDuration() {
            given(mediaPlayer.mediaDurationInMillis()).willReturn(ONE_SECOND_IN_MILLIS);
            long videoDurationInMillis = player.mediaDurationInMillis();

            assertThat(videoDurationInMillis).isEqualTo(ONE_SECOND_IN_MILLIS);
        }

        @Test
        public void whenGettingBufferPercentage_thenReturnsMediaPlayerBufferPercentage() {
            int mediaPlayerBufferPercentage = 10;
            given(mediaPlayer.getBufferPercentage()).willReturn(mediaPlayerBufferPercentage);

            int bufferPercentage = player.bufferPercentage();

            assertThat(bufferPercentage).isEqualTo(mediaPlayerBufferPercentage);
        }

        @Test
        public void whenGettingPlayerInformation_thenReturnsMediaPlayerInformation() {
            PlayerInformation playerInformation = player.getPlayerInformation();

            assertThat(playerInformation).isEqualTo(mediaPlayerInformation);
        }

        @Test
        public void whenAttachingPlayerView_thenAddsVideoSizeChangedListener() {
            NoPlayer.VideoSizeChangedListener videoSizeChangedListener = mock(NoPlayer.VideoSizeChangedListener.class);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            player.attach(playerView);

            verify(listenersHolder).addVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenAttachingPlayerView_thenAddsStateChangedListener() {
            NoPlayer.StateChangedListener stateChangedListener = mock(NoPlayer.StateChangedListener.class);
            given(playerView.getStateChangedListener()).willReturn(stateChangedListener);
            player.attach(playerView);

            verify(listenersHolder).addStateChangedListener(stateChangedListener);
        }

        @Test
        public void whenAttachingPlayerView_thenPreventsVideoDriverBug() {
            player.attach(playerView);

            verify(buggyVideoDriverPreventer).preventVideoDriverBug(player, containerView);
        }

        @Test
        public void whenDetachingPlayerView_thenRemovesVideoSizeChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            NoPlayer.VideoSizeChangedListener videoSizeChangedListener = mock(NoPlayer.VideoSizeChangedListener.class);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            player.detach(playerView);

            verify(listenersHolder).removeVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenDetachingPlayerView_thenRemovesStateChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            NoPlayer.StateChangedListener stateChangedListener = mock(NoPlayer.StateChangedListener.class);
            given(playerView.getStateChangedListener()).willReturn(stateChangedListener);
            player.detach(playerView);

            verify(listenersHolder).removeStateChangedListener(stateChangedListener);
        }

        @Test
        public void whenDetachingPlayerView_thenClearsVideoDriverBugPreventer() {
            PlayerView playerView = mock(PlayerView.class);
            View containerView = mock(View.class);
            given(playerView.getContainerView()).willReturn(containerView);
            player.detach(playerView);

            verify(buggyVideoDriverPreventer).clear(containerView);
        }

        @Test
        public void whenSelectingAudioTrack_thenDelegatesToMediaPlayer() {
            PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

            player.selectAudioTrack(audioTrack);

            verify(mediaPlayer).selectAudioTrack(audioTrack);
        }

        @Test
        public void whenGettingAudioTracks_thenDelegatesToMediaPlayer() {
            given(mediaPlayer.getAudioTracks()).willReturn(AUDIO_TRACKS);
            AudioTracks audioTracks = player.getAudioTracks();

            assertThat(audioTracks).isEqualTo(AUDIO_TRACKS);
        }

        @Test
        public void whenStopping_thenPlayerResourcesAreReleased_andNotListeners() {

            player.stop();

            verify(delayedActionExecutor).clearAllActions();
            verify(listenersHolder).resetState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void whenReleasing_thenPlayerResourcesAreReleased() {

            player.release();

            verify(delayedActionExecutor).clearAllActions();
            verify(listenersHolder).resetState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder).clear();
        }

        @Test
        public void givenAttachedPlayerView_whenStopping_thenPlayerResourcesAreReleased_andNotListeners() {
            player.attach(playerView);

            player.stop();

            verify(delayedActionExecutor).clearAllActions();
            verify(listenersHolder).resetState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(containerView).setVisibility(View.GONE);
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenAttachedPlayerView_whenReleasing_thenPlayerResourcesAreReleased() {
            player.attach(playerView);

            player.release();

            verify(delayedActionExecutor).clearAllActions();
            verify(listenersHolder).resetState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(containerView).setVisibility(View.GONE);
            verify(listenersHolder).clear();
        }
    }

    public static class GivenPlayerIsAttached extends Base {

        private static final long DELAY_MILLIS = 500;
        private static final boolean IS_NOT_PLAYING = false;
        private static final float ANY_VOLUME = 0.4f;

        @Override
        public void setUp() {
            super.setUp();
            player.attach(playerView);
        }

        @Test
        public void whenLoadingVideo_thenNotifiesBufferStateListenersThatBufferStarted() {
            player.loadVideo(URI, OPTIONS);

            verify(bufferStateListener).onBufferStarted();
        }

        @Test
        public void whenLoadingVideo_thenPreparesVideo() {
            player.loadVideo(URI, OPTIONS);

            verify(mediaPlayer).prepareVideo(URI, surface);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenNotifiesBufferStateListenersThatBufferStarted() {
            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(bufferStateListener).onBufferStarted();
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenPreparesVideo() {
            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(mediaPlayer).prepareVideo(URI, surface);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenStartsTimeout() {
            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(loadTimeout).start(ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);
        }

        @Test
        public void whenLoadingVideo_thenShowsContainerView() {
            player.loadVideo(URI, OPTIONS);

            verify(containerView).setVisibility(View.VISIBLE);
        }

        @Test
        public void whenStartingPlay_thenStartsBeatingHeart() {
            player.play();

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlay_thenMediaPlayerStarts() {
            player.play();

            verify(mediaPlayer).start(surface);
        }

        @Test
        public void whenStartingPlay_thenNotifiesStateListenersThatVideoIsPlaying() {
            player.play();

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenStartsBeatingHeart() {
            given(mediaPlayer.currentPositionInMillis()).willReturn((int) BEGINNING_POSITION);

            player.playAt(BEGINNING_POSITION);

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenMediaPlayerStarts() {
            given(mediaPlayer.currentPositionInMillis()).willReturn((int) BEGINNING_POSITION);

            player.playAt(BEGINNING_POSITION);

            verify(mediaPlayer).start(surface);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenersThatVideoIsPlaying() {
            given(mediaPlayer.currentPositionInMillis()).willReturn((int) BEGINNING_POSITION);

            player.playAt(BEGINNING_POSITION);

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideo_thenPlayerIsReleased_andNotListeners() {
            given(mediaPlayer.hasPlayedContent()).willReturn(true);

            player.loadVideo(URI, OPTIONS);

            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreReleased_andNotListeners() {
            given(mediaPlayer.hasPlayedContent()).willReturn(true);

            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideo_thenPlayerResourcesAreNotReleased() {
            given(mediaPlayer.hasPlayedContent()).willReturn(false);

            player.loadVideo(URI, OPTIONS);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(mediaPlayer, never()).release();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreNotReleased() {
            given(mediaPlayer.hasPlayedContent()).willReturn(false);

            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(mediaPlayer, never()).release();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenNotifiesBufferStateListenersThatBufferStarted() {
            long differentPositionInMillis = givenPositionThatDiffersFromPlayheadPosition();

            player.playAt(differentPositionInMillis);

            verify(bufferStateListener).onBufferStarted();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenInitialisesPlaybackForSeeking() {
            long differentPositionInMillis = givenPositionThatDiffersFromPlayheadPosition();

            player.playAt(differentPositionInMillis);

            thenInitialisesPlaybackForSeeking();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenSeeksToVideoPosition() {
            long differentPositionInMillis = givenPositionThatDiffersFromPlayheadPosition();

            player.playAt(differentPositionInMillis);
            ArgumentCaptor<DelayedActionExecutor.Action> argumentCaptor = ArgumentCaptor.forClass(DelayedActionExecutor.Action.class);
            verify(delayedActionExecutor).performAfterDelay(argumentCaptor.capture(), eq(DELAY_MILLIS));
            argumentCaptor.getValue().perform();

            verify(mediaPlayer).seekTo(differentPositionInMillis);
        }

        @Test
        public void givenPlayerIsAlreadyPlaying_whenPlaying_thenNotifiesVideoPlaying() {
            given(mediaPlayer.isPlaying()).willReturn(IS_NOT_PLAYING);

            player.play();

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void whenSetRepeating_thenSetRepeating() {
            player.setRepeating(false);

            verify(mediaPlayer).setRepeating(false);
        }

        @Test
        public void whenSetVolume_thenSetVolumeOnMediaPlayer() {
            player.setVolume(ANY_VOLUME);

            verify(mediaPlayer).setVolume(ANY_VOLUME);
        }

        @Test
        public void whenGetVolume_thenReturnMediaPlayerVolume() {
            given(mediaPlayer.getVolume()).willReturn(ANY_VOLUME);

            float currentVolume = player.getVolume();

            assertThat(currentVolume).isEqualTo(ANY_VOLUME);
        }

        private long givenPositionThatDiffersFromPlayheadPosition() {
            given(mediaPlayer.currentPositionInMillis()).willReturn(0);
            return 1;
        }

        private void thenInitialisesPlaybackForSeeking() {
            InOrder inOrder = inOrder(mediaPlayer);

            inOrder.verify(mediaPlayer).start(surface);
            inOrder.verify(mediaPlayer).pause();
            inOrder.verifyNoMoreInteractions();
        }
    }

    public abstract static class Base {

        static final Options OPTIONS = new OptionsBuilder().withContentType(ContentType.H264).build();
        static final long BEGINNING_POSITION = 0;
        static final Uri URI = Mockito.mock(Uri.class);
        static final int TEN_SECONDS = 10;
        static final Timeout ANY_TIMEOUT = Timeout.fromSeconds(TEN_SECONDS);
        static final NoPlayer.LoadTimeoutCallback ANY_LOAD_TIMEOUT_CALLBACK = new NoPlayer.LoadTimeoutCallback() {
            @Override
            public void onLoadTimeout() {

            }
        };

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        MediaPlayerInformation mediaPlayerInformation;
        @Mock
        AndroidMediaPlayerFacade mediaPlayer;
        @Mock
        MediaPlayerForwarder forwarder;
        @Mock
        PlayerListenersHolder listenersHolder;
        @Mock
        CheckBufferHeartbeatCallback checkBufferHeartbeatCallback;
        @Mock
        LoadTimeout loadTimeout;
        @Mock
        Heart heart;
        @Mock
        DelayedActionExecutor delayedActionExecutor;
        @Mock
        BuggyVideoDriverPreventer buggyVideoDriverPreventer;
        @Mock
        NoPlayer.PreparedListener preparedListener;
        @Mock
        NoPlayer.BufferStateListener bufferStateListener;
        @Mock
        NoPlayer.ErrorListener errorListener;
        @Mock
        NoPlayer.CompletionListener completionListener;
        @Mock
        NoPlayer.VideoSizeChangedListener videoSizeChangedListener;
        @Mock
        NoPlayer.InfoListener infoListener;
        @Mock
        NoPlayer.StateChangedListener stateChangedListener;
        @Mock
        Either<Surface, SurfaceHolder> surface;
        @Mock
        PlayerView playerView;
        @Mock
        NoPlayer.StateChangedListener stateChangeListener;
        @Mock
        MediaPlayer.OnPreparedListener onPreparedListener;
        @Mock
        MediaPlayer.OnCompletionListener onCompletionListener;
        @Mock
        MediaPlayer.OnErrorListener onErrorListener;
        @Mock
        MediaPlayer.OnVideoSizeChangedListener onSizeChangedListener;
        @Mock
        CheckBufferHeartbeatCallback.BufferListener bufferListener;
        @Mock
        View containerView;
        @Mock
        PlayerSurfaceHolder playerSurfaceHolder;

        AndroidMediaPlayerImpl player;

        @Before
        public void setUp() {
            NoPlayerLog.setLoggingEnabled(false);
            SurfaceRequester surfaceRequester = mock(SurfaceRequester.class);
            given(playerView.getPlayerSurfaceHolder()).willReturn(playerSurfaceHolder);
            given(playerSurfaceHolder.getSurfaceRequester()).willReturn(surfaceRequester);
            given(playerView.getStateChangedListener()).willReturn(stateChangeListener);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            given(playerView.getContainerView()).willReturn(containerView);
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) {
                    SurfaceRequester.Callback callback = invocation.getArgument(0);
                    callback.onSurfaceReady(surface);
                    return null;
                }
            }).when(surfaceRequester).requestSurface(any(SurfaceRequester.Callback.class));

            given(listenersHolder.getPreparedListeners()).willReturn(preparedListener);
            given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListener);
            given(listenersHolder.getErrorListeners()).willReturn(errorListener);
            given(listenersHolder.getCompletionListeners()).willReturn(completionListener);
            given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListener);
            given(listenersHolder.getInfoListeners()).willReturn(infoListener);
            given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListener);

            given(forwarder.onPreparedListener()).willReturn(onPreparedListener);
            given(forwarder.onCompletionListener()).willReturn(onCompletionListener);
            given(forwarder.onErrorListener()).willReturn(onErrorListener);
            given(forwarder.onSizeChangedListener()).willReturn(onSizeChangedListener);
            given(forwarder.onHeartbeatListener()).willReturn(bufferListener);

            player = new AndroidMediaPlayerImpl(
                    mediaPlayerInformation,
                    mediaPlayer,
                    forwarder,
                    listenersHolder,
                    checkBufferHeartbeatCallback,
                    loadTimeout,
                    heart,
                    delayedActionExecutor,
                    buggyVideoDriverPreventer
            );
        }
    }
}
