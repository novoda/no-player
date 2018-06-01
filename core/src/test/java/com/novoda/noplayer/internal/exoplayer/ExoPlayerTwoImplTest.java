package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import android.view.View;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.text.Cue;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayer.StateChangedListener;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerSurfaceHolder;
import com.novoda.noplayer.PlayerType;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.TextCues;
import com.novoda.noplayer.model.Timeout;
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

import java.util.Arrays;
import java.util.List;

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

    private static final Options OPTIONS = new OptionsBuilder().withContentType(ContentType.DASH).build();
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

            verify(listenersHolder).resetState();
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

            int actualWidth = player.videoWidth();
            int actualHeight = player.videoHeight();

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

            verify(listenersHolder).resetState();
            verify(stateChangedListener).onVideoStopped();
            verify(loadTimeout).cancel();
            verify(heart).stopBeatingHeart();
            verify(exoPlayerFacade).release();
        }

        @Test
        public void whenReleasing_thenPlayerResourcesAreReleased() {

            player.release();

            verify(listenersHolder).resetState();
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

            verify(listenersHolder).resetState();
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

            verify(listenersHolder).resetState();
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

            player.loadVideo(uri, OPTIONS);

            verify(exoPlayerFacade).loadVideo(playerView.getPlayerSurfaceHolder(), drmSessionCreator, uri, OPTIONS, forwarder, mediaCodecSelector);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenDelegatesLoadingToFacade() {
            player.attach(playerView);

            player.loadVideoWithTimeout(uri, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(exoPlayerFacade).loadVideo(playerView.getPlayerSurfaceHolder(), drmSessionCreator, uri, OPTIONS, forwarder, mediaCodecSelector);
        }

        @Test
        public void whenLoadingVideoWithTimeout_thenStartsLoadTimeout() {
            player.attach(playerView);

            player.loadVideoWithTimeout(uri, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

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

            player.loadVideo(uri, OPTIONS);

            verify(containerView).setVisibility(View.VISIBLE);
        }

        @Test
        public void givenPlayerHasPlayedVideo_whenLoadingVideo_thenPlayerIsReleased_andNotListeners() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(true);
            player.attach(playerView);

            player.loadVideo(URI, OPTIONS);

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

            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

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

            player.loadVideo(URI, OPTIONS);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(exoPlayerFacade, never()).release();
        }

        @Test
        public void givenPlayerHasNotPlayedVideo_whenLoadingVideoWithTimeout_thenPlayerResourcesAreNotReleased() {
            given(exoPlayerFacade.hasPlayedContent()).willReturn(false);
            player.attach(playerView);

            player.loadVideoWithTimeout(URI, OPTIONS, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

            verify(stateChangedListener, never()).onVideoStopped();
            verify(loadTimeout, never()).cancel();
            verify(heart, never()).stopBeatingHeart();
            verify(exoPlayerFacade, never()).release();
        }
    }

    public static class GivenAttachedAndVideoIsLoaded extends Base {

        private static final float ANY_VOLUME = 0.4f;

        @Override
        public void setUp() {
            super.setUp();
            player.attach(playerView);
            player.loadVideo(uri, OPTIONS);
        }

        @Test
        public void whenLoadingVideo_thenAddsStateChangedListenerToListenersHolder() {

            player.loadVideo(uri, OPTIONS);

            verify(listenersHolder).addStateChangedListener(playerView.getStateChangedListener());
        }

        @Test
        public void whenLoadingVideo_thenAddsVideoSizeChangedListenerToListenersHolder() {

            player.loadVideo(uri, OPTIONS);

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
            long seekPositionInMillis = TWO_MINUTES_IN_MILLIS;

            player.seekTo(seekPositionInMillis);

            verify(exoPlayerFacade).seekTo(seekPositionInMillis);
        }

        @Test
        public void whenStartingPlayback_andSurfaceHolderIsReady_thenPlaysFacadeWithSurfaceHolder() {
            player.play();

            verify(exoPlayerFacade).play();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenSeeksToPosition() {
            player.playAt(TWO_MINUTES_IN_MILLIS);

            verify(exoPlayerFacade).seekTo(TWO_MINUTES_IN_MILLIS);
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenStartsBeatingHeart() {
            player.playAt(TWO_MINUTES_IN_MILLIS);

            verify(heart).startBeatingHeart();
        }

        @Test
        public void whenStartingPlay_thenNotifiesStateListenersThatVideoIsPlaying() {

            player.play();

            verify(stateChangedListener).onVideoPlaying();
        }

        @Test
        public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenersThatVideoIsPlaying() {
            player.playAt(TWO_MINUTES_IN_MILLIS);

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
                public Object answer(InvocationOnMock invocation) {
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

        @Test
        public void whenSetRepeating_thenSetRepeating() {
            player.setRepeating(false);

            verify(exoPlayerFacade).setRepeating(false);
        }

        @Test
        public void whenSetVolume_thenSetVolumeOnExoPlayer() {
            player.setVolume(ANY_VOLUME);

            verify(exoPlayerFacade).setVolume(ANY_VOLUME);
        }

        @Test
        public void whenGetVolume_thenReturnVolumeFromExoPlayer() {
            given(exoPlayerFacade.getVolume()).willReturn(ANY_VOLUME);

            float currentVolume = player.getVolume();

            assertThat(currentVolume).isEqualTo(ANY_VOLUME);
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
        DrmSessionCreator drmSessionCreator;
        @Mock
        MediaCodecSelector mediaCodecSelector;
        @Mock
        View containerView;
        @Mock
        PlayerSurfaceHolder playerSurfaceHolder;

        ExoPlayerTwoImpl player;

        @Before
        public void setUp() {
            given(playerView.getPlayerSurfaceHolder()).willReturn(playerSurfaceHolder);
            given(playerView.getStateChangedListener()).willReturn(stateChangeListener);
            given(playerView.getVideoSizeChangedListener()).willReturn(videoSizeChangedListener);
            given(playerView.getContainerView()).willReturn(containerView);

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
