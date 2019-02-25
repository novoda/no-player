package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;

import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class NoPlayerExoPlayerCreatorTest {

    private static final boolean USE_SECURE_CODEC = true;
    private static final boolean ALLOW_CROSS_PROTOCOL_REDIRECTS = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerTwoImpl player;
    @Mock
    private Context context;
    @Mock
    private DrmSessionCreator drmSessionCreator;
    @Mock
    private NoPlayerExoPlayerCreator.InternalCreator internalCreator;

    private NoPlayerExoPlayerCreator creator;

    @Before
    public void setUp() {
        given(internalCreator.create(context, drmSessionCreator, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS)).willReturn(player);
        creator = new NoPlayerExoPlayerCreator(internalCreator);
    }

    @Test
    public void whenCreatingExoPlayerTwo_thenInitialisesPlayer() {
        creator.createExoPlayer(context, drmSessionCreator, USE_SECURE_CODEC, ALLOW_CROSS_PROTOCOL_REDIRECTS);

        verify(player).initialise();
    }
}
