package com.novoda.noplayer.internal.mediaplayer;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class NoPlayerMediaPlayerCreatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NoPlayerMediaPlayerCreator.InternalCreator internalCreator;
    @Mock
    private AndroidMediaPlayerImpl player;
    @Mock
    private Context context;

    private NoPlayerMediaPlayerCreator creator;

    @Before
    public void setUp() {
        creator = new NoPlayerMediaPlayerCreator(internalCreator);
        given(internalCreator.create(context)).willReturn(player);
    }

    @Test
    public void whenCreatingMediaPlayer_thenInitialisesPlayer() {
        creator.createMediaPlayer(context);

        verify(player).initialise();
    }
}
