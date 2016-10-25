package com.novoda.noplayer.mediaplayer;

import android.view.View;

import com.novoda.noplayer.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BuggyDriverPreventerTest {

    @Mock
    private PlayerChecker playerChecker;
    @Mock
    private View container;

    @Mock
    private Player player;

    private BuggyVideoDriverPreventer buggyVideoDriverPreventer;

    @Before
    public void setUp() {
        initMocks(this);

        buggyVideoDriverPreventer = new BuggyVideoDriverPreventer(container, player, playerChecker);
    }

    @Test
    public void givenMediaPlayerCanBeBuggyThenListenerIsAttachedToVideoView() {
        givenMediaPlayerCanBeBuggy();

        buggyVideoDriverPreventer.preventVideoDriverBug();

        verify(container).addOnLayoutChangeListener(any(View.OnLayoutChangeListener.class));
    }

    @Test
    public void givenMediaPlayerIsNotBuggyThenListenerIsNotAttachedToVideoView() {
        givenMediaPlayerIsNotBuggy();

        buggyVideoDriverPreventer.preventVideoDriverBug();

        verifyZeroInteractions(container);
    }

    private void givenMediaPlayerCanBeBuggy() {
        when(playerChecker.getPlayerType()).thenReturn(AndroidMediaPlayerType.AWESOME);
    }

    private void givenMediaPlayerIsNotBuggy() {
        when(playerChecker.getPlayerType()).thenReturn(AndroidMediaPlayerType.NU);
    }

}
