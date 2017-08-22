package com.novoda.noplayer.internal.mediaplayer;

import android.os.Handler;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.Clock;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.Timeout;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoadTimeoutTest {

    private static final Timeout ANY_TIME = Timeout.fromSeconds(0);

    @Mock
    private Clock clock;

    @Mock
    private Handler handler;

    @InjectMocks
    private LoadTimeout loadTimeout;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void whenStartingATimeout_thenAnyPreviouslySetTimeoutRunnableAreRemoved() {

        loadTimeout.start(ANY_TIME, any(NoPlayer.LoadTimeoutCallback.class));

        verify(handler).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void whenStartingATimeout_thenTheTimeoutRunnableIsPosted() {

        loadTimeout.start(ANY_TIME, any(NoPlayer.LoadTimeoutCallback.class));

        verify(handler).post(any(Runnable.class));
    }

    @Test
    public void whenCancelingATimeout_thenTimeoutRunnableIsRemoved() {

        loadTimeout.cancel();

        verify(handler).removeCallbacks(any(Runnable.class));
    }
}
