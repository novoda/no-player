package com.novoda.noplayer;

import android.os.Handler;

import com.novoda.noplayer.internal.Clock;
import com.novoda.noplayer.model.LoadTimeout;
import com.novoda.noplayer.model.Timeout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoadTimeoutTest {

    private static final long START_TIME = 0L;
    private static final long END_TIME = 1000L;
    private static final int RESCHEDULE_DELAY_MILLIS = 1000;
    private static final Timeout TIMEOUT_NOT_REACHED = Timeout.fromSeconds(5);
    private static final Timeout TIMEOUT_REACHED = Timeout.fromSeconds(1);

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Clock clock;

    @Mock
    Handler handler;

    @Mock
    NoPlayer.LoadTimeoutCallback loadTimeoutCallback;

    private LoadTimeout loadTimeout;

    @Before
    public void setUp() {
        loadTimeout = new LoadTimeout(clock, handler);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();
                return null;
            }
        }).when(handler).post(any(Runnable.class));
    }

    @Test
    public void givenTimeoutIsReached_whenStarting_thenOnLoadTimeoutIsCalled() {
        when(clock.getCurrentTime()).thenReturn(START_TIME, END_TIME);

        loadTimeout.start(TIMEOUT_REACHED, loadTimeoutCallback);

        verify(loadTimeoutCallback).onLoadTimeout();
    }

    @Test
    public void givenTimeoutIsNotReached_whenStarting_thenTimeoutIsRescheduled() {
        when(clock.getCurrentTime()).thenReturn(START_TIME, END_TIME);

        loadTimeout.start(TIMEOUT_NOT_REACHED, loadTimeoutCallback);

        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(handler).post(captor.capture());
        verify(handler).postDelayed(captor.getValue(), RESCHEDULE_DELAY_MILLIS);
    }
}
