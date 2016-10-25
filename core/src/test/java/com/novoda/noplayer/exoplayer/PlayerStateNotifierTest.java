package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ExoPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PlayerStateNotifierTest {

    private static final boolean PLAY_WHEN_READY = true;
    private static final boolean DO_NOT_PLAY_WHEN_READY = false;
    private static final boolean RENDERER_IS_BUILT = true;
    private static final boolean RENDERER_IS_NOT_BUILT = false;
    private static final boolean RENDERER_IS_BUILDING = true;
    private static final boolean RENDERER_IS_NOT_BUILDING = false;
    private static final int NO_INTERACTIONS = 0;
    private static final int ONE_INTERACTION = 1;

    @Parameters(name = "#{index}: given playWhenReady is {0}, playBackState is {1}, rendererIsBuilt is {2} and rendererIsBuilding={3} " +
            "when maybeReportPlayerState then the ExoPlayerFacade.Listeners are notified {6} time(s) of a state change with playWhenReady " +
            "being {4} and state being {5}.")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // 0.
                {PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, NO_INTERACTIONS},

                // 8.
                {PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, NO_INTERACTIONS},

                // 16.
                {PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_BUFFERING, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, NO_INTERACTIONS},

                // 24.
                {PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_ENDED, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, NO_INTERACTIONS},

                // 32.
                {PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_READY, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, PLAY_WHEN_READY, ExoPlayer.STATE_READY, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_NOT_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_READY, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_BUILT, RENDERER_IS_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_PREPARING, ONE_INTERACTION},
                {DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_READY, RENDERER_IS_NOT_BUILT, RENDERER_IS_NOT_BUILDING, DO_NOT_PLAY_WHEN_READY, ExoPlayer.STATE_IDLE, NO_INTERACTIONS},
        });
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Parameter(0)
    public boolean playWhenReady;

    @Parameter(1)
    public int playBackState;

    @Parameter(2)
    public boolean rendererIsBuilt;

    @Parameter(3)
    public boolean rendererIsBuilding;

    @Parameter(4)
    public boolean expectedPlayWhenReady;

    @Parameter(5)
    public int expectedState;

    @Parameter(6)
    public int expectedListenerInteractions;

    @Mock
    private ExoPlayer exoPlayer;

    @Mock
    private RendererState rendererState;

    @InjectMocks
    private PlayerStateNotifier playerStateNotifier;

    @Mock
    private ExoPlayerFacade.Listener exoPlayerListener;

    private List<ExoPlayerFacade.Listener> listeners;

    @Before
    public void setUp() {
        listeners = Collections.singletonList(exoPlayerListener);
    }

    @Test
    public void givenExoPlayerAndRendererState_whenPossiblyReportingPlayerStateChange_thenTheListenersAreNotifiedAppropriately() {
        when(exoPlayer.getPlayWhenReady()).thenReturn(playWhenReady);
        when(exoPlayer.getPlaybackState()).thenReturn(playBackState);
        when(rendererState.isBuilding()).thenReturn(rendererIsBuilding);
        when(rendererState.isBuilt()).thenReturn(rendererIsBuilt);

        playerStateNotifier.maybeReportPlayerState(listeners);

        verify(exoPlayerListener, VerificationModeFactory.times(expectedListenerInteractions)).onStateChanged(expectedPlayWhenReady, expectedState);
    }
}
