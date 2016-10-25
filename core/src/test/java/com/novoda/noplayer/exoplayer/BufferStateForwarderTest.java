package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlayer;
import com.novoda.noplayer.listeners.BufferStateListeners;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(Parameterized.class)
public class BufferStateForwarderTest {

    private static final boolean ANY_PLAY_WHEN_READY = true;

    @Parameters(name = "#{index}: given the exoPlayerState is {0} then we forward the buffer as being {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ExoPlayer.STATE_PREPARING, BufferNotified.BUFFER_STARTED},
                {ExoPlayer.STATE_BUFFERING, BufferNotified.BUFFER_STARTED},
                {ExoPlayer.STATE_READY, BufferNotified.BUFFER_COMPLETED},
                {ExoPlayer.STATE_ENDED, BufferNotified.NO_BUFFER_EVENT},
                {ExoPlayer.STATE_IDLE, BufferNotified.NO_BUFFER_EVENT},
        });
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Parameter(0)
    public int exoPlayerState;

    @Parameter(1)
    public BufferNotified bufferNotified;

    @Mock
    private BufferStateListeners bufferStateListeners;

    @InjectMocks
    private BufferStateForwarder bufferStateForwarder;

    @Test
    public void givenBufferStateListeners_whenOnStateChanged_thenListenersAreForwardedTheCorrectEvent() {

        bufferStateForwarder.onStateChanged(ANY_PLAY_WHEN_READY, exoPlayerState);

        verifyBufferNotified();
    }

    private void verifyBufferNotified() {
        if (bufferNotified == BufferNotified.BUFFER_STARTED) {
            verify(bufferStateListeners).onBufferStarted();
        } else if (bufferNotified == BufferNotified.BUFFER_COMPLETED) {
            verify(bufferStateListeners).onBufferCompleted();
        } else {
            verifyZeroInteractions(bufferStateListeners);
        }
    }

    private enum BufferNotified {
        BUFFER_STARTED,
        BUFFER_COMPLETED,
        NO_BUFFER_EVENT
    }
}
