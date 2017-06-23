package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImpl;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImplFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ExoPlayerTwoCreatorTest {

    private static final boolean USE_SECURE_CODEC = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ExoPlayerTwoImplFactory factory;
    @Mock
    private ExoPlayerTwoImpl player;
    @Mock
    private Context context;
    @Mock
    private DrmSessionCreator drmSessionCreator;

    private PlayerFactory.ExoPlayerCreator creator;

    @Before
    public void setUp() {
        creator = new PlayerFactory.ExoPlayerCreator(factory);
        given(factory.create(context, drmSessionCreator, USE_SECURE_CODEC)).willReturn(player);
    }

    @Test
    public void whenCreatingExoPlayerTwo_thenInitialisesPlayer() {
        creator.createExoPlayer(context, drmSessionCreator, USE_SECURE_CODEC);

        verify(player).initialise();
    }
}
