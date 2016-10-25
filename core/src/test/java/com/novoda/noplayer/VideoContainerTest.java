package com.novoda.noplayer;

import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class VideoContainerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private View container;

    @InjectMocks
    private VideoContainer videoContainer;

    @Test
    public void givenAContainer_whenShowingIt_thenItIsSetAsVisible() {

        videoContainer.show();

        verify(container).setVisibility(View.VISIBLE);
    }

    @Test
    public void givenNoContainer_whenShowingIt_thenItIsSetAsGone() {

        videoContainer.hide();

        verify(container).setVisibility(View.GONE);
    }
}
