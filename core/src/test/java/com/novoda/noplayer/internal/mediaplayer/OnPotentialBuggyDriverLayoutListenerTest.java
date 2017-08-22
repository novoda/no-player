package com.novoda.noplayer.internal.mediaplayer;

import android.view.View;

import com.novoda.noplayer.NoPlayer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class OnPotentialBuggyDriverLayoutListenerTest {

    private static final int ANY_DIMENSION_VALUE = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    NoPlayer player;

    @InjectMocks
    OnPotentialBuggyDriverLayoutListener buggyDriverLayoutListener;

    @Test
    public void givenStatusIsNotCorrupted_whenALayoutChangeOccurs_thenDoNotForceAlignNativeMediaPlayerStatus() {
        when(player.isPlaying()).thenReturn(false);

        onLayoutChange();

        verify(player, never()).play();
    }

    @Test
    public void givenStatusMightBeNotCorrupted_whenALayoutChangeOccurs_thenForceAlignNativeMediaPlayerStatus() {
        when(player.isPlaying()).thenReturn(true);

        onLayoutChange();

        verify(player, atLeastOnce()).play();
    }

    private void onLayoutChange() {
        buggyDriverLayoutListener.onLayoutChange(
                mock(View.class),
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE,
                ANY_DIMENSION_VALUE);
    }
}
