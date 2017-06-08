package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;
import android.os.Handler;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.listeners.BufferStateListeners;
import com.novoda.noplayer.listeners.CompletionListeners;
import com.novoda.noplayer.listeners.ErrorListeners;
import com.novoda.noplayer.listeners.InfoListeners;
import com.novoda.noplayer.listeners.PreparedListeners;
import com.novoda.noplayer.listeners.StateChangedListeners;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;
import com.novoda.noplayer.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.notils.logger.simple.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AndroidMediaPlayerImplCreatorTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 200;
    private static final int ANY_ROTATION_DEGREES = 0;
    private static final int ANY_PIXEL_WIDTH_HEIGHT = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private AndroidMediaPlayerFacade mediaPlayer;
    @Mock
    private PlayerListenersHolder listenersHolder;
    @Mock
    private MediaPlayerForwarder forwarder;
    @Mock
    private LoadTimeout loadTimeout;
    @Mock
    private Heart heart;
    @Mock
    private Handler handler;
    @Mock
    private CheckBufferHeartbeatCallback bufferHeartbeatCallback;
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
    @Mock
    private MediaPlayer.OnPreparedListener onPreparedListener;
    @Mock
    private MediaPlayer.OnCompletionListener onCompletionListener;
    @Mock
    private MediaPlayer.OnErrorListener onErrorListener;
    @Mock
    private MediaPlayer.OnVideoSizeChangedListener onSizeChangedListener;
    @Mock
    private CheckBufferHeartbeatCallback.BufferListener bufferListener;

    private AndroidMediaPlayerImplCreator creator;

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

        given(forwarder.onPreparedListener()).willReturn(onPreparedListener);
        given(forwarder.onCompletionListener()).willReturn(onCompletionListener);
        given(forwarder.onErrorListener()).willReturn(onErrorListener);
        given(forwarder.onSizeChangedListener()).willReturn(onSizeChangedListener);
        given(forwarder.onHeartbeatListener()).willReturn(bufferListener);

        creator = new AndroidMediaPlayerImplCreator();
    }

    @Test
    public void whenCreatingAndroidMediaPlayerImpl_thenBindsListenersToForwarder() {
        AndroidMediaPlayerImpl player = creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);

        verify(forwarder).bind(preparedListeners, player);
        verify(forwarder).bind(bufferStateListeners, errorListeners, player);
        verify(forwarder).bind(completionListeners, stateChangedListeners);
        verify(forwarder).bind(videoSizeChangedListeners);
        verify(forwarder).bind(infoListeners);
    }

    @Test
    public void whenCreatingAndroidMediaPlayerImpl_thenBindsListenerToBufferHeartbeatCallback() {
        creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);

        verify(bufferHeartbeatCallback).bind(bufferListener);
    }

    @Test
    public void whenCreatingAndroidMediaPlayerImpl_thenBindsHeartbeatCallbackToListenerHolder() {
        creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);

        verify(listenersHolder).addHeartbeatCallback(bufferHeartbeatCallback);
    }

    @Test
    public void givenBoundPreparedListener_whenCallingOnPrepared_thenCancelsTimeout() {
        AndroidMediaPlayerImpl player = creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);

        ArgumentCaptor<Player.PreparedListener> preparedListenerCaptor = ArgumentCaptor.forClass(Player.PreparedListener.class);
        verify(listenersHolder).addPreparedListener(preparedListenerCaptor.capture());

        Player.PreparedListener preparedListener = preparedListenerCaptor.getValue();
        preparedListener.onPrepared(player);

        verify(loadTimeout).cancel();
    }

    @Test
    public void whenCreatingAndroidMediaPlayerImpl_thenBindsHeart() {
        creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);

        verify(heart).bind(any(Heart.Heartbeat.class));
    }

    @Test
    public void givenBoundPreparedListener_whenCallingOnPrepared_thenSetsOnSeekCompleteListener() {
        AndroidMediaPlayerImpl player = creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);
        ArgumentCaptor<Player.PreparedListener> preparedListenerCaptor = ArgumentCaptor.forClass(Player.PreparedListener.class);
        verify(listenersHolder).addPreparedListener(preparedListenerCaptor.capture());

        Player.PreparedListener preparedListener = preparedListenerCaptor.getValue();
        preparedListener.onPrepared(player);

        verify(mediaPlayer).setOnSeekCompleteListener(any(MediaPlayer.OnSeekCompleteListener.class));
    }

    @Test
    public void givenBoundErrorListener_whenCallingOnError_thenCancelsTimeout() {
        AndroidMediaPlayerImpl player = creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);
        ArgumentCaptor<Player.ErrorListener> errorListenerCaptor = ArgumentCaptor.forClass(Player.ErrorListener.class);
        verify(listenersHolder).addErrorListener(errorListenerCaptor.capture());

        Player.ErrorListener errorListener = errorListenerCaptor.getValue();
        errorListener.onError(player, mock(Player.PlayerError.class));

        verify(loadTimeout).cancel();
    }

    @Test
    public void givenBoundVideoSizeChangedListener_whenCallingOnVideoSizeChanged_thenVideoWidthAndHeightMatches() {
        AndroidMediaPlayerImpl player = creator.create(loadTimeout, forwarder, mediaPlayer, listenersHolder, bufferHeartbeatCallback, heart, preventer, handler);
        ArgumentCaptor<Player.VideoSizeChangedListener> videoSizeChangedListenerCaptor = ArgumentCaptor.forClass(Player.VideoSizeChangedListener.class);
        verify(listenersHolder).addVideoSizeChangedListener(videoSizeChangedListenerCaptor.capture());

        Player.VideoSizeChangedListener videoSizeChangedListener = videoSizeChangedListenerCaptor.getValue();
        videoSizeChangedListener.onVideoSizeChanged(WIDTH, HEIGHT, ANY_ROTATION_DEGREES, ANY_PIXEL_WIDTH_HEIGHT);

        assertThat(player.getVideoWidth()).isEqualTo(WIDTH);
        assertThat(player.getVideoHeight()).isEqualTo(HEIGHT);
    }
}
