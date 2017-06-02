package com.novoda.noplayer.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerAudioTrack;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.PlayerView;
import com.novoda.noplayer.Timeout;
import com.novoda.noplayer.VideoContainer;
import com.novoda.noplayer.VideoDuration;
import com.novoda.noplayer.VideoPosition;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;
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

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ExoPlayerTwoImplTest {

    private static final long TWO_MINUTES_IN_MILLIS = 120000;
    private static final long TEN_MINUTES_IN_MILLIS = 600000;
    private static final long TEN_SECONDS = 10;

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int TEN_PERCENT = 10;
    private static final int ANY_ROTATION_DEGREES = 30;
    private static final int ANY_PIXEL_WIDTH_HEIGHT = 75;

    private static final boolean IS_PLAYING = true;
    private static final boolean PLAY_WHEN_READY = true;
    private static final boolean DO_NOT_PLAY_WHEN_READY = false;
    private static final boolean IS_BEATING = true;
    private static final boolean IS_NOT_BEATING = false;
    private static final boolean RESET_POSITION = true;
    private static final boolean DO_NOT_RESET_STATE = false;

    private static final ContentType ANY_CONTENT_TYPE = ContentType.DASH;
    private static final Timeout ANY_TIMEOUT = Timeout.fromSeconds(TEN_SECONDS);
    private static final Player.LoadTimeoutCallback ANY_LOAD_TIMEOUT_CALLBACK = new Player.LoadTimeoutCallback() {
        @Override
        public void onLoadTimeout() {

        }
    };
    private static final PlayerAudioTrack PLAYER_AUDIO_TRACK = new PlayerAudioTrack(0, 0, "id", "english", ".mp4", 1, 120);
    private static final List<PlayerAudioTrack> AUDIO_TRACKS = Collections.singletonList(PLAYER_AUDIO_TRACK);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SimpleExoPlayer internalExoPlayer;
    @Mock
    private MediaSourceFactory mediaSourceFactory;
    @Mock
    private ExoPlayerForwarder exoPlayerForwarder;
    @Mock
    private LoadTimeout loadTimeout;
    @Mock
    private ExoPlayerAudioTrackSelector trackSelector;
    @Mock
    private Heart heart;
    @Mock
    private VideoContainer videoContainer;
    @Mock
    private Uri uri;
    @Mock
    private PlayerView playerView;
    @Mock
    private PlayerListenersHolder listenersHolder;

    @Mock
    private ErrorListeners errorListeners;
    @Mock
    private PreparedListeners preparedListeners;
    @Mock
    private BufferStateListeners bufferStateListeners;
    @Mock
    private CompletionListeners completionListeners;
    @Mock
    private StateChangedListeners stateChangedListeners;
    @Mock
    private InfoListeners infoListeners;
    @Mock
    private VideoSizeChangedListeners videoSizeChangedListeners;
    @Mock
    private BitrateChangedListeners bitrateChangedListeners;
    @Mock
    Player.PreReleaseListener preReleaseListener;

    private Player player;

    @Before
    public void setUp() {
        given(listenersHolder.getErrorListeners()).willReturn(errorListeners);
        given(listenersHolder.getPreparedListeners()).willReturn(preparedListeners);
        given(listenersHolder.getBufferStateListeners()).willReturn(bufferStateListeners);
        given(listenersHolder.getCompletionListeners()).willReturn(completionListeners);
        given(listenersHolder.getStateChangedListeners()).willReturn(stateChangedListeners);
        given(listenersHolder.getInfoListeners()).willReturn(infoListeners);
        given(listenersHolder.getVideoSizeChangedListeners()).willReturn(videoSizeChangedListeners);
        given(listenersHolder.getBitrateChangedListeners()).willReturn(bitrateChangedListeners);
        given(listenersHolder.getPlayerReleaseListener()).willReturn(preReleaseListener);

        player = new ExoPlayerTwoImpl(
                internalExoPlayer,
                listenersHolder,
                mediaSourceFactory,
                exoPlayerForwarder,
                loadTimeout,
                trackSelector,
                heart,
                videoContainer
        );
    }

    @Test
    public void whenCreatingExoPlayerTwoImpl_thenCallsBindForHeart() {
        verify(heart).bind(any(Heart.Heartbeat.class));
    }

    @Test
    public void whenCreatingExoPlayerTwoImpl_thenCallsBindForAllListeners() {
        verify(exoPlayerForwarder).bind(any(PreparedListeners.class), eq(player));
        verify(exoPlayerForwarder).bind(any(CompletionListeners.class));
        verify(exoPlayerForwarder).bind(any(ErrorListeners.class), eq(player));
        verify(exoPlayerForwarder).bind(any(BufferStateListeners.class));
        verify(exoPlayerForwarder).bind(any(VideoSizeChangedListeners.class));
        verify(exoPlayerForwarder).bind(any(BitrateChangedListeners.class));
        verify(exoPlayerForwarder).bind(any(InfoListeners.class));
    }

    @Test
    public void givenBoundPreparedListeners_whenCallingOnPrepared_thenCancelsTimeout() {
        ArgumentCaptor<Player.PreparedListener> argumentCaptor = ArgumentCaptor.forClass(Player.PreparedListener.class);

        verify(listenersHolder).addPreparedListener(argumentCaptor.capture());
        Player.PreparedListener preparedListener = argumentCaptor.getValue();
        preparedListener.onPrepared(player);

        verify(loadTimeout).cancel();
    }

    @Test
    public void givenBoundErrorListeners_whenCallingOnError_thenCancelsTimeout() {
        ArgumentCaptor<Player.ErrorListener> argumentCaptor = ArgumentCaptor.forClass(Player.ErrorListener.class);

        verify(listenersHolder).addErrorListener(argumentCaptor.capture());
        Player.ErrorListener errorListener = argumentCaptor.getValue();
        errorListener.onError(player, mock(Player.PlayerError.class));

        verify(loadTimeout).cancel();
    }

    @Test
    public void givenBoundVideoChangedListeners_whenCallingOnVideoSizeChanged_thenVideoWidthAndHeightMatches() {
        ArgumentCaptor<Player.VideoSizeChangedListener> argumentCaptor = ArgumentCaptor.forClass(Player.VideoSizeChangedListener.class);
        verify(listenersHolder).addVideoSizeChangedListener(argumentCaptor.capture());

        Player.VideoSizeChangedListener videoSizeChangedListener = argumentCaptor.getValue();
        videoSizeChangedListener.onVideoSizeChanged(WIDTH, HEIGHT, ANY_ROTATION_DEGREES, ANY_PIXEL_WIDTH_HEIGHT);

        int actualWidth = player.getVideoWidth();
        int actualHeight = player.getVideoHeight();

        assertThat(actualWidth).isEqualTo(WIDTH);
        assertThat(actualHeight).isEqualTo(HEIGHT);
    }

    @Test
    public void givenExoPlayerIsReadyToPlay_whenCallingIsPlaying_thenReturnsTrue() {
        given(internalExoPlayer.getPlayWhenReady()).willReturn(IS_PLAYING);

        boolean isPlaying = player.isPlaying();

        assertThat(isPlaying).isTrue();
    }

    @Test
    public void whenGettingPlayheadPosition_thenReturnsCurrentPosition() {
        given(internalExoPlayer.getCurrentPosition()).willReturn(TWO_MINUTES_IN_MILLIS);

        VideoPosition playheadPosition = player.getPlayheadPosition();

        assertThat(playheadPosition).isEqualTo(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));
    }

    @Test
    public void whenGettingMediaDuration_thenReturnsDuration() {
        given(internalExoPlayer.getDuration()).willReturn(TEN_MINUTES_IN_MILLIS);

        VideoDuration videoDuration = player.getMediaDuration();

        assertThat(videoDuration).isEqualTo(VideoDuration.fromMillis(TEN_MINUTES_IN_MILLIS));
    }

    @Test
    public void whenGettingBufferPercentage_thenReturnsBufferPercentage() {
        given(internalExoPlayer.getBufferedPercentage()).willReturn(TEN_PERCENT);

        int bufferPercentage = player.getBufferPercentage();

        assertThat(bufferPercentage).isEqualTo(TEN_PERCENT);
    }

    @Test
    public void whenStartingPlay_thenShowsVideoContainer() {
        player.play();

        verify(videoContainer).show();
    }

    @Test
    public void whenStartingPlay_thenStartsBeatingHeart() {
        player.play();

        verify(heart).startBeatingHeart();
    }

    @Test
    public void whenStartingPlay_thenSetsPlayWhenReadyToTrue() {
        player.play();

        verify(internalExoPlayer).setPlayWhenReady(PLAY_WHEN_READY);
    }

    @Test
    public void whenStartingPlay_thenNotifiesStateListenerThatVideoIsPlaying() {
        player.play();

        verify(stateChangedListeners).onVideoPlaying();
    }

    @Test
    public void whenStartingPlayAtVideoPosition_thenSeeksToPosition() {
        player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

        verify(internalExoPlayer).seekTo(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS).inMillis());
    }

    @Test
    public void whenStartingPlayAtVideoPosition_thenShowsVideoContainer() {
        player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

        verify(videoContainer).show();
    }

    @Test
    public void whenStartingPlayAtVideoPosition_thenStartsBeatingHeart() {
        player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

        verify(heart).startBeatingHeart();
    }

    @Test
    public void whenStartingPlayAtVideoPosition_thenSetsPlayWhenReadyToTrue() {
        player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

        verify(internalExoPlayer).setPlayWhenReady(PLAY_WHEN_READY);
    }

    @Test
    public void whenStartingPlayAtVideoPosition_thenNotifiesStateListenerThatVideoIsPlaying() {
        player.play(VideoPosition.fromMillis(TWO_MINUTES_IN_MILLIS));

        verify(stateChangedListeners).onVideoPlaying();
    }

    @Test
    public void whenPausing_thenSetsPlayWhenReadyToFalse() {
        player.pause();

        verify(internalExoPlayer).setPlayWhenReady(DO_NOT_PLAY_WHEN_READY);
    }

    @Test
    public void whenPausing_thenNotifiesStateListenerThatVideoIsPaused() {
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

        verify(internalExoPlayer).seekTo(videoPosition.inMillis());
    }

    @Test
    public void whenStopping_thenStopsPlayer() {
        player.stop();

        verify(internalExoPlayer).stop();
    }

    @Test
    public void whenReleasing_thenNotifiesReleaseListenerOfPlayerPreRelease() {
        player.release();

        verify(preReleaseListener).onPlayerPreRelease(player);
    }

    @Test
    public void whenReleasing_thenNotifiesStateStateListenerThatVideoHasReleased() {
        player.release();

        verify(stateChangedListeners).onVideoReleased();
    }

    @Test
    public void whenReleasing_thenCancelsLoadTimeout() {
        player.release();

        verify(loadTimeout).cancel();
    }

    @Test
    public void whenReleasing_thenStopsBeatingHeart() {
        player.release();

        verify(heart).stopBeatingHeart();
    }

    @Test
    public void whenReleasing_thenReleasesUnderlyingPlayer() {
        player.release();

        verify(internalExoPlayer).release();
    }

    @Test
    public void whenReleasing_thenHidesVideoContainer() {
        player.release();

        verify(videoContainer).hide();
    }

    @Test
    public void whenLoadingVideo_thenResetsPreparedListeners() {
        player.loadVideo(uri, ANY_CONTENT_TYPE);

        verify(preparedListeners).reset();
    }

    @Test
    public void whenLoadingVideo_thenShowsVideoContainer() {
        player.loadVideo(uri, ANY_CONTENT_TYPE);

        verify(videoContainer).show();
    }

    @Test
    public void whenLoadingVideo_thenAddsPlayerEventListener() {
        player.loadVideo(uri, ANY_CONTENT_TYPE);

        verify(internalExoPlayer).addListener(exoPlayerForwarder.exoPlayerEventListener());
    }

    @Test
    public void whenLoadingVideo_thenSetsVideoDebugListener() {
        player.loadVideo(uri, ANY_CONTENT_TYPE);

        verify(internalExoPlayer).setVideoDebugListener(exoPlayerForwarder.videoRendererEventListener());
    }

    @Test
    public void givenMediaSource_whenLoadingVideo_thenPreparesInternalExoPlayer() {
        MediaSource mediaSource = givenMediaSource();

        player.loadVideo(uri, ANY_CONTENT_TYPE);

        verify(internalExoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    @Test
    public void whenLoadingVideoWithTimeout_thenResetsPreparedListeners() {
        player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

        verify(preparedListeners).reset();
    }

    @Test
    public void whenLoadingVideoWithTimeout_thenShowsVideoContainer() {
        player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

        verify(videoContainer).show();
    }

    @Test
    public void whenLoadingVideoWithTimeout_thenAddsPlayerEventListener() {
        player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

        verify(internalExoPlayer).addListener(exoPlayerForwarder.exoPlayerEventListener());
    }

    @Test
    public void whenLoadingVideoWithTimeout_thenSetsVideoDebugListener() {
        player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

        verify(internalExoPlayer).setVideoDebugListener(exoPlayerForwarder.videoRendererEventListener());
    }

    @Test
    public void givenMediaSource_whenLoadingVideoWithTimeout_thenPreparesInternalExoPlayer() {
        MediaSource mediaSource = givenMediaSource();

        player.loadVideoWithTimeout(uri, ANY_CONTENT_TYPE, ANY_TIMEOUT, ANY_LOAD_TIMEOUT_CALLBACK);

        verify(internalExoPlayer).prepare(mediaSource, RESET_POSITION, DO_NOT_RESET_STATE);
    }

    @Test
    public void whenGettingPlayerInformation_thenReturnsPlayerInformation() {
        PlayerInformation playerInformation = player.getPlayerInformation();

        assertThat(playerInformation.getPlayerType()).isEqualTo(PlayerType.EXO_PLAYER);
        assertThat(playerInformation.getVersion()).isEqualTo(ExoPlayerLibraryInfo.VERSION);
    }

    @Ignore("VideoContainer is switched out in ExoPlayerTwoImpl, need to detect that it has been swapped.")
    @Test
    public void whenAttaching_thenAddsPlayerViewToVideoContainer() {
        player.attach(mock(PlayerView.class));
    }

    @Ignore("SimplePlayerView is a final class, we should probably pass something else to test this.")
    @Test
    public void whenAttaching_thenSetsPlayerForPlayerView() {
        player.attach(mock(PlayerView.class));
    }

    @Test
    public void whenSelectingAudioTrack_thenDelegatesToTrackSelector() {
        PlayerAudioTrack audioTrack = mock(PlayerAudioTrack.class);

        player.selectAudioTrack(audioTrack);

        verify(trackSelector).selectAudioTrack(audioTrack);
    }

    @Test
    public void whenGettingAudioTracks_thenDelegatesToTrackSelector() {
        given(trackSelector.getAudioTracks()).willReturn(AUDIO_TRACKS);
        List<PlayerAudioTrack> audioTracks = player.getAudioTracks();

        assertThat(audioTracks).isEqualTo(AUDIO_TRACKS);
    }

    private MediaSource givenMediaSource() {
        MediaSource mediaSource = mock(MediaSource.class);
        given(mediaSourceFactory.create(
                ANY_CONTENT_TYPE,
                uri,
                exoPlayerForwarder.extractorMediaSourceListener(),
                exoPlayerForwarder.mediaSourceEventListener()
              )
        ).willReturn(mediaSource);

        return mediaSource;
    }
}
