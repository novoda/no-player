package com.novoda.noplayer.exoplayer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AspectRatioChangeListenerTest {

    @Mock
    private AspectRatioChangeListener.Listener mockListener;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void whenAspectRatioChanges_thenNewAspectRatioIsCorrect() {
        AspectRatioChangeListener aspectRatioChangeListener = new AspectRatioChangeListener(mockListener);
        aspectRatioChangeListener.onVideoSizeChanged(10, 5, 0, 2);

        verify(mockListener).onNewAspectRatio(4);
    }

    @Test
    public void whenAspectRatioChandgesToZero_thenNewAspectRatioIsCorrect() {
        AspectRatioChangeListener aspectRatioChangeListener = new AspectRatioChangeListener(mockListener);
        aspectRatioChangeListener.onVideoSizeChanged(10, 5, 0, 0);

        verify(mockListener).onNewAspectRatio(0);
    }

    @Test
    public void whenAspectRatioChangesToNine_thenNewAspectRatioIsCorrect() {
        AspectRatioChangeListener aspectRatioChangeListener = new AspectRatioChangeListener(mockListener);
        aspectRatioChangeListener.onVideoSizeChanged(3, 18, 7, 9);

        verify(mockListener).onNewAspectRatio(1.5f);
    }
}
