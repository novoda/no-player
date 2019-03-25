package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.os.Handler;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.internal.utils.Optional;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExoPlayerListenerTest {

    private final SimpleExoPlayer exoPlayer = mock(SimpleExoPlayer.class);
    private final Handler handler = mock(Handler.class);
    private final ExoPlayerListener exoPlayerListener = new ExoPlayerListener(Optional.<AdvertsLoader>absent(), handler);

    @Test
    public void addsPlayerEventListener() {
        exoPlayerListener.bind(exoPlayer);

        verify(exoPlayer).addListener(exoPlayerListener);
    }

    @Test
    public void addsAnalyticsListener() {
        exoPlayerListener.bind(exoPlayer);

        verify(exoPlayer).addAnalyticsListener(exoPlayerListener);
    }

    @Test
    public void addsVideoListener() {
        exoPlayerListener.bind(exoPlayer);

        verify(exoPlayer).addVideoListener(exoPlayerListener);
    }

}
