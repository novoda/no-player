package com.novoda.noplayer.mediaplayer;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.SurfaceHolderRequester;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.noplayer.player.PlayerInformation;
import com.novoda.noplayer.player.PlayerType;
import com.novoda.notils.logger.simple.Log;

import java.util.Collections;
import java.util.List;

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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class AndroidMediaPlayerImplTest {

    public static class GivenPlayer extends Base {

        private static final boolean IS_BEATING = true;
        private static final boolean IS_NOT_BEATING = false;

        private static final long TWO_MINUTES_IN_MILLIS = 120000;

        private static final int ONE_SECOND_IN_MILLIS = 1000;
        private static final boolean IS_PLAYING = true;
        private static final PlayerAudioTrack PLAYER_AUDIO_TRACK = new PlayerAudioTrack(0, 0, "id", "english", ".mp4", 1, 120);
        private static final List<PlayerAudioTrack> AUDIO_TRACKS = Collections.singletonList(PLAYER_AUDIO_TRACK);

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        private AndroidMediaPlayerFacade mediaPlayer;
        @Mock
        private PlayerListenersHolder listenersHolder;
        @Mock
        private LoadTimeout loadTimeout;
        @Mock
        private Heart heart;
        @Mock
        private Handler handler;
        @Mock
        private BuggyVideoDriverPreventer preventer;
        @Mock
        private PreparedListeners preparedListeners;
        @Mock
        private BufferStateListeners bufferStateListeners;
        @Mock
        private ErrorListeners errorListeners;
        @Mock
        private CompletionListeners completionListeners;
        @Mock
        private VideoSizeChangedListeners videoSizeChangedListeners;
        @Mock
        private InfoListeners infoListeners;
        @Mock
        private StateChangedListeners stateChangedListeners;

        private AndroidMediaPlayerImpl player;

        @Before
        public void setUp() {
            Log.setShowLogs(false);
            given(listenersHolder.getPreparedListeners()).willReturn(preparedListeners);
            given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListeners);
            given(listenersHolder.getErrorListeners()).willReturn(errorListeners);
            given(listenersHolder.getCompletionListeners()).willReturn(completionListeners);
            given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListeners);
            given(listenersHolder.getInfoListeners()).willReturn(infoListeners);
            given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListeners);

            player = new AndroidMediaPlayerImpl(mediaPlayer, listenersHolder, loadTimeout, heart, handler, preventer);
        }

        @Test
        public void givenAndroidMediaPlayerIsPlaying_whenCallingIsPlaying_thenReturnsTrue() {
            given(mediaPlayer.isPlaying()).willReturn(IS_PLAYING);

            boolean isPlaying = player.isPlaying();

            assertThat(isPlaying).isTrue();
        }

        @Test
        public void whenSeeking_thenSeeksToPosition() {
            VideoPosition videoPosition = VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS);

            player.seekTo(videoPosition);

            verify(mediaPlayer).seekTo(videoPosition.inImpreciseMillis());
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

            verify(stateChangedListeners).onVideoPaused();
        }

        @Test
        public void givenPlayerIsNotSeeking_whenGettingPlayheadPosition_thenReturnsCurrentMediaPlayerPosition() {
            given(mediaPlayer.getCurrentPosition()).willReturn(ONE_SECOND_IN_MILLIS);
            VideoPosition playheadPosition = player.getPlayheadPosition();

            assertThat(playheadPosition.inMillis()).isEqualTo(ONE_SECOND_IN_MILLIS);
        }

        @Test
        public void givenPlayerIsSeeking_whenGettingPlayheadPosition_thenReturnsSeekPosition() {
            VideoPosition seekPosition = VideoPosition.fromSeconds(TEN_SECONDS);
            player.seekTo(seekPosition);

            VideoPosition videoPosition = player.getPlayheadPosition();

            assertThat(videoPosition).isEqualTo(seekPosition);
        }

        @Test
        public void whenGettingMediaDuration_thenReturnsMediaPlayerDuration() {
            given(mediaPlayer.getDuration()).willReturn(ONE_SECOND_IN_MILLIS);
            VideoDuration videoDuration = player.getMediaDuration();

            assertThat(videoDuration).isEqualTo(VideoDuration.fromMillis(ONE_SECOND_IN_MILLIS));
        }

        @Test
        public void whenGettingBufferPercentage_thenReturnsMediaPlayerBufferPercentage() {
            int mediaPlayerBufferPercentage = 10;
            given(mediaPlayer.getBufferPercentage()).willReturn(mediaPlayerBufferPercentage);

            int bufferPercentage = player.getBufferPercentage();

            assertThat(bufferPercentage).isEqualTo(mediaPlayerBufferPercentage);
        }

        @Test
        public void whenGettingPlayerInformation_thenReturnsMediaPlayerInformation() {
            PlayerInformation playerInformation = player.getPlayerInformation();

            assertThat(playerInformation.getPlayerType()).isEqualTo(PlayerType.MEDIA_PLAYER);
            assertThat(playerInformation.getVersion()).isEqualTo(Build.VERSION.RELEASE);
        }

        @Test
        public void whenAttachingPlayerView_thenAddsVideoSizeChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            Player.VideoSizeChangedListener videoSizeChangedListener = mock(Player.VideoSizeChangedListener.class);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            player.attach(playerView);

            verify(listenersHolder).addVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenAttachingPlayerView_thenAddsStateChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            Player.StateChangedListener stateChangedListener = mock(Player.StateChangedListener.class);
            given(playerView.getStateChangedListener()).willReturn(stateChangedListener);
            player.attach(playerView);

            verify(listenersHolder).addStateChangedListener(stateChangedListener);
        }

        @Test
        public void whenAttachingPlayerView_thenPreventsVideoDriverBug() {
            PlayerView playerView = mock(PlayerView.class);
            View containerView = mock(View.class);
            given(playerView.getContainerView()).willReturn(containerView);
            player.attach(playerView);

            verify(preventer).preventVideoDriverBug(player, containerView);
        }

        @Test
        public void whenDetachingPlayerView_thenRemovesVideoSizeChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            Player.VideoSizeChangedListener videoSizeChangedListener = mock(Player.VideoSizeChangedListener.class);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            player.detach(playerView);

            verify(listenersHolder).removeVideoSizeChangedListener(videoSizeChangedListener);
        }

        @Test
        public void whenDetachingPlayerView_thenRemovesStateChangedListener() {
            PlayerView playerView = mock(PlayerView.class);
            Player.StateChangedListener stateChangedListener = mock(Player.StateChangedListener.class);
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

            verify(preventer).clear(containerView);
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
            List<PlayerAudioTrack> audioTracks = player.getAudioTracks();

            assertThat(audioTracks).isEqualTo(AUDIO_TRACKS);
        }

        @Test
        public void whenStopping_thenPlayerResourcesAreReleased_andNotListeners() {

            player.stop();

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void whenReleasing_thenPlayerResourcesAreReleased() {

            player.release();

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder).clear();
        }
    }

    public static class GivenPlayerIsAttached extends Base {

        private static final long DELAY_MILLIS = 500;

        @Override
        public void setUp() {
            super.setUp();
            player.attach(playerView);
        }

        @Test
        public void whenLoadingVideo_thenNotifiesBufferStateListenersThatBufferStarted() {
            player.loadVideo(URI, ContentType.HLS);

            verify(bufferStateListeners).onBufferStarted();
        }

        @Test
        public void whenLoadingVideo_thenPreparesVideo() {
            player.loadVideo(URI, ContentType.HLS);

            verify(mediaPlayer).prepareVideo(URI, surfaceHolder);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenNotifiesBufferStateListenersThatBufferStarted() {
            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(bufferStateListeners).onBufferStarted();
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenPreparesVideo() {
            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(mediaPlayer).prepareVideo(URI, surfaceHolder);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenStartsTimeout() {
            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(loadTimeout).start(ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);
        }

        @Test
        public void whenStartingPlay_thenStartsBeatingHeart() {
            player.play();

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlay_thenMediaPlayerStarts() {
            player.play();

            verify(mediaPlayer).start(surfaceHolder);
        }

        @Test
        public void whenStartingPlay_thenNotifiesStateListenersThatVideoIsPlaying() {
            player.play();

            verify(stateChangedListeners).onVideoPlaying();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenStartsBeatingHeart() {
            VideoPosition videoPosition = VideoPosition.BEGINNING;
            given(mediaPlayer.getCurrentPosition()).willReturn(videoPosition.inImpreciseMillis());

            player.play(videoPosition);

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenMediaPlayerStarts() {
            VideoPosition videoPosition = VideoPosition.BEGINNING;
            given(mediaPlayer.getCurrentPosition()).willReturn(videoPosition.inImpreciseMillis());

            player.play(videoPosition);

            verify(mediaPlayer).start(surfaceHolder);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenersThatVideoIsPlaying() {
            VideoPosition videoPosition = VideoPosition.BEGINNING;
            given(mediaPlayer.getCurrentPosition()).willReturn(videoPosition.inImpreciseMillis());

            player.play(videoPosition);

            verify(stateChangedListeners).onVideoPlaying();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideo_thenPlayerIsReleased_andNotListeners() {
            given(mediaPlayer.hasPlayedContent()).willReturn(true);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreReleased_andNotListeners() {
            given(mediaPlayer.hasPlayedContent()).willReturn(true);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListeners).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(mediaPlayer).release();
            verify(listenersHolder, never()).clear();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideo_thenPlayerResourcesAreNotReleased() {
            given(mediaPlayer.hasPlayedContent()).willReturn(false);

            player.loadVideo(URI, ContentType.HLS);

            verify(stateChangedListeners, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(mediaPlayer, never()).release();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreNotReleased() {
            given(mediaPlayer.hasPlayedContent()).willReturn(false);

            player.loadVideoWithTimeout(URI, ContentType.HLS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListeners, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(mediaPlayer, never()).release();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenNotifiesBufferStateListenersThatBufferStarted() {
            VideoPosition differentPosition = givenPositionThatDiffersFromPlayheadPosition();

            player.play(differentPosition);

            verify(bufferStateListeners).onBufferStarted();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenInitialisesPlaybackForSeeking() {
            VideoPosition differentPosition = givenPositionThatDiffersFromPlayheadPosition();

            player.play(differentPosition);

            thenInitialisesPlaybackForSeeking();
        }

        @Test
        public void givenPositionThatDiffersFromPlayheadPosition_whenStartingPlayAtVideoPosition_thenSeeksToVideoPosition() {
            VideoPosition differentPosition = givenPositionThatDiffersFromPlayheadPosition();

            player.play(differentPosition);
            ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
            verify(handler).postDelayed(runnableCaptor.capture(), eq(DELAY_MILLIS));
            runnableCaptor.getValue().run();

            verify(mediaPlayer).seekTo(differentPosition.inImpreciseMillis());
        }

        private VideoPosition givenPositionThatDiffersFromPlayheadPosition() {
            given(mediaPlayer.getCurrentPosition()).willReturn(VideoPosition.BEGINNING.inImpreciseMillis());
            return VideoPosition.fromMillis(1);
        }

        private void thenInitialisesPlaybackForSeeking() {
            InOrder inOrder = inOrder(mediaPlayer);

            inOrder.verify(mediaPlayer).start(surfaceHolder);
            inOrder.verify(mediaPlayer).pause();
            inOrder.verifyNoMoreInteractions();
        }
    }

    public static abstract class Base {

        static final Uri URI = Mockito.mock(Uri.class);
        static final int TEN_SECONDS = 10;
        static final Timeout ANY_TIMEOUT = Timeout.fromSeconds(TEN_SECONDS);
        static final Player.LoadTimeoutCallback ANY_LOAD_TIMEOUT_CALLBACK = new Player.LoadTimeoutCallback() {
            @Override
            public void onLoadTimeout() {

            }
        };

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        AndroidMediaPlayerFacade mediaPlayer;
        @Mock
        PlayerListenersHolder listenersHolder;
        @Mock
        LoadTimeout loadTimeout;
        @Mock
        Heart heart;
        @Mock
        Handler handler;
        @Mock
        BuggyVideoDriverPreventer buggyVideoDriverPreventer;
        @Mock
        PreparedListeners preparedListeners;
        @Mock
        BufferStateListeners bufferStateListeners;
        @Mock
        ErrorListeners errorListeners;
        @Mock
        CompletionListeners completionListeners;
        @Mock
        VideoSizeChangedListeners videoSizeChangedListeners;
        @Mock
        InfoListeners infoListeners;
        @Mock
        StateChangedListeners stateChangedListeners;
        @Mock
        SurfaceHolder surfaceHolder;
        @Mock
        PlayerView playerView;
        @Mock
        Player.StateChangedListener stateChangeListener;
        @Mock
        Player.VideoSizeChangedListener videoSizeChangedListener;

        AndroidMediaPlayerImpl player;

        @Before
        public void setUp() {
            Log.setShowLogs(false);
            SurfaceHolderRequester surfaceHolderRequester = mock(SurfaceHolderRequester.class);
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

            given(listenersHolder.getPreparedListeners()).willReturn(preparedListeners);
            given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListeners);
            given(listenersHolder.getErrorListeners()).willReturn(errorListeners);
            given(listenersHolder.getCompletionListeners()).willReturn(completionListeners);
            given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListeners);
            given(listenersHolder.getInfoListeners()).willReturn(infoListeners);
            given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListeners);

            player = new AndroidMediaPlayerImpl(mediaPlayer, listenersHolder, loadTimeout, heart, handler, buggyVideoDriverPreventer);
        }
    }
}
