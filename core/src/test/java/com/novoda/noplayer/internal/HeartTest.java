package com.novoda.noplayer.internal;

import android.os.Handler;

import com.novoda.noplayer.NoPlayer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.mock;

public class HeartTest {

    private final NoPlayer.HeartbeatCallback heartbeatCallback = mock(NoPlayer.HeartbeatCallback.class);
    private final NoPlayer noPlayer = mock(NoPlayer.class);
    private final Handler handler = mock(Handler.class);

    private Heart heart;

    @Before
    public void setUp() {
        will(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();
                return null;
            }
        }).given(handler).post(any(Runnable.class));

        heart = Heart.newInstance(handler);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException_whenStartingHeartWithoutBindingAction() {
        heart.startBeatingHeart();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException_whenForcingBeatWithoutBindingAction() {
        heart.forceBeat();
    }

    @Test
    public void removesCallbacks_whenStartingHeart() {
        Heart.Heartbeat onHeartbeat = new Heart.Heartbeat(heartbeatCallback, noPlayer);
        heart.bind(onHeartbeat);

        heart.startBeatingHeart();

        then(handler).should().removeCallbacks(any(Runnable.class));
    }

    @Test
    public void schedulesNextBeat_whenStartingHeart() {
        Heart.Heartbeat onHeartbeat = new Heart.Heartbeat(heartbeatCallback, noPlayer);
        heart.bind(onHeartbeat);

        heart.startBeatingHeart();

        then(handler).should().postDelayed(any(Runnable.class), anyLong());
    }

    @Test
    public void doesNotEmitOnBeat_whenPlayerIsNotPlaying() {
        Heart.Heartbeat onHeartbeat = new Heart.Heartbeat(heartbeatCallback, noPlayer);
        heart.bind(onHeartbeat);

        heart.startBeatingHeart();

        then(heartbeatCallback).shouldHaveZeroInteractions();
    }

    @Test
    public void emitsOnBeat_whenPlayerIsPlaying() {
        given(noPlayer.isPlaying()).willReturn(true);
        Heart.Heartbeat onHeartbeat = new Heart.Heartbeat(heartbeatCallback, noPlayer);
        heart.bind(onHeartbeat);

        heart.startBeatingHeart();

        then(heartbeatCallback).should().onBeat(noPlayer);
    }

    @Test
    public void emitsOnBeat_whenForcingBeat() {
        given(noPlayer.isPlaying()).willReturn(true);
        Heart.Heartbeat onHeartbeat = new Heart.Heartbeat(heartbeatCallback, noPlayer);
        heart.bind(onHeartbeat);

        heart.forceBeat();

        then(heartbeatCallback).should().onBeat(noPlayer);
    }
}
