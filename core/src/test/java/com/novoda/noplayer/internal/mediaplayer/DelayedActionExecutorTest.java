package com.novoda.noplayer.internal.mediaplayer;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DelayedActionExecutorTest {

    private static final long ANY_DELAY_IN_MILLIS = 10;

    private final DelayedActionExecutor.Action action = mock(DelayedActionExecutor.Action.class);
    private final DelayedActionExecutor.Action secondaryAction = mock(DelayedActionExecutor.Action.class);

    private final Handler immediatelyExecutingHandler = createImmediatelyExecutingHandler();
    private final Handler nonExecutingHandler = mock(Handler.class);

    private final Map<DelayedActionExecutor.Action, Runnable> runnables = new HashMap<>();

    private DelayedActionExecutor delayedActionExecutor;

    @Test
    public void whenActionIsNotPerformedYet_thenMapContainsAction() {
        delayedActionExecutor = new DelayedActionExecutor(nonExecutingHandler, runnables);

        delayedActionExecutor.performAfterDelay(action, ANY_DELAY_IN_MILLIS);

        assertThat(runnables).hasSize(1);
    }

    @Test
    public void whenPerformingActionAfterDelay_thenRemovesActionFromMap() {
        delayedActionExecutor = new DelayedActionExecutor(immediatelyExecutingHandler, runnables);

        delayedActionExecutor.performAfterDelay(action, ANY_DELAY_IN_MILLIS);

        assertThat(runnables).isEmpty();
    }

    @Test
    public void whenPerformingActionAfterDelay_thenPerformsAction() {
        delayedActionExecutor = new DelayedActionExecutor(immediatelyExecutingHandler, runnables);

        delayedActionExecutor.performAfterDelay(action, ANY_DELAY_IN_MILLIS);

        verify(action).perform();
    }

    @Test
    public void givenMultipleQueuedActions_whenClearingActions_thenRemovesAllActions() {
        delayedActionExecutor = new DelayedActionExecutor(nonExecutingHandler, runnables);
        delayedActionExecutor.performAfterDelay(action, ANY_DELAY_IN_MILLIS);
        delayedActionExecutor.performAfterDelay(secondaryAction, ANY_DELAY_IN_MILLIS);

        delayedActionExecutor.clearAllActions();

        assertThat(runnables).isEmpty();
        verify(nonExecutingHandler, times(2)).removeCallbacks(any(Runnable.class));
    }

    private Handler createImmediatelyExecutingHandler() {
        Handler handler = mock(Handler.class);
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        willAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                argumentCaptor.getValue().run();
                return null;
            }
        }).given(handler).postDelayed(argumentCaptor.capture(), eq(ANY_DELAY_IN_MILLIS));
        return handler;
    }
}
