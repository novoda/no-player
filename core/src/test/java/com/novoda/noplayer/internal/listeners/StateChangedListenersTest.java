package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.utils.NoPlayerLog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class StateChangedListenersTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NoPlayer.StateChangedListener stateChangedListener;

    private StateChangedListeners stateChangedListeners;

    @Before
    public void setUp() {
        NoPlayerLog.setLoggingEnabled(false);
        stateChangedListeners = new StateChangedListeners();
        stateChangedListeners.add(stateChangedListener);
    }

    @Test
    public void whenDoubleCallingOnVideoPlaying_thenEmitsOnlyFirstOnVideoPlayingEvent() {
        stateChangedListeners.onVideoPlaying();
        stateChangedListeners.onVideoPlaying();

        verify(stateChangedListener).onVideoPlaying();
    }

    @Test
    public void whenDoubleCallingOnVideoPaused_thenEmitsOnlyFirstOnVideoPausedEvent() {
        stateChangedListeners.onVideoPaused();
        stateChangedListeners.onVideoPaused();

        verify(stateChangedListener).onVideoPaused();
    }

    @Test
    public void whenDoubleCallingOnVideoStopped_thenEmitsOnlyFirstOnVideoStoppedEvent() {
        stateChangedListeners.onVideoStopped();
        stateChangedListeners.onVideoStopped();

        verify(stateChangedListener).onVideoStopped();
    }
}
