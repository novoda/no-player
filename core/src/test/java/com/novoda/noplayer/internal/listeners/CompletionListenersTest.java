package com.novoda.noplayer.internal.listeners;

import com.novoda.noplayer.NoPlayer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class CompletionListenersTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NoPlayer.CompletionListener completionListener;

    private CompletionListeners completionListeners;

    @Before
    public void setUp() {
        completionListeners = new CompletionListeners();
        completionListeners.add(completionListener);
    }

    @Test
    public void whenCallingOnCompletion_thenNotifiesOnCompletion() {
        completionListeners.onCompletion();

        verify(completionListener).onCompletion();
    }

    @Test
    public void whenCallingOnCompletionTwice_thenDoesNothing() {
        completionListeners.onCompletion();
        reset(completionListener);

        completionListeners.onCompletion();

        verify(completionListener, never()).onCompletion();
    }
}
