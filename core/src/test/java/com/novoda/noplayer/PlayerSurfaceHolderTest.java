package com.novoda.noplayer;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import com.google.android.exoplayer2.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PlayerSurfaceHolderTest {

    @Mock
    private SurfaceView surfaceView;
    @Mock
    private SurfaceHolder surfaceHolder;
    @Mock
    private TextureView textureView;
    @Mock
    private Player.VideoComponent videoPlayer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        surfaceHolder = mock(SurfaceHolder.class);
        given(surfaceView.getHolder()).willReturn(surfaceHolder);
    }

    @Test
    public void whenCreatingPlayerSurfaceHolderWithSurfaceView_thenAttachCallbackToSurfaceHolder() {

        PlayerSurfaceHolder.create(surfaceView);

        verify(surfaceHolder).addCallback(any(PlayerViewSurfaceHolder.class));
    }

    @Test
    public void whenCreatingPlayerSurfaceHolderWithTextureView_thenAttachSurfaceTextureListenerToTextureView() {

        PlayerSurfaceHolder.create(textureView);

        verify(textureView).setSurfaceTextureListener(any(PlayerViewSurfaceHolder.class));
    }

    @Test
    public void givenPlayerSurfaceHolderContainsSurfaceView_whenAttachingVideoPlayer_thenSetsVideoSurfaceView() {
        PlayerSurfaceHolder playerSurfaceHolder = PlayerSurfaceHolder.create(surfaceView);

        playerSurfaceHolder.attach(videoPlayer);

        verify(videoPlayer).setVideoSurfaceView(surfaceView);
    }

    @Test
    public void givenPlayerSurfaceHolderContainsTextureView_whenAttachingVideoPlayer_thenSetsVideoTextureView() {
        PlayerSurfaceHolder playerSurfaceHolder = PlayerSurfaceHolder.create(textureView);

        playerSurfaceHolder.attach(videoPlayer);

        verify(videoPlayer).setVideoTextureView(textureView);
    }

    @Test
    public void givenPlayerSurfaceHolderContainsNoView_whenAttachingVideoPlayer_thenThrowsException() {
        thrown.expect(IllegalArgumentException.class);

        PlayerSurfaceHolder playerSurfaceHolder = new PlayerSurfaceHolder(null, null, null);

        playerSurfaceHolder.attach(videoPlayer);
    }
}
