package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.listeners.VideoSizeChangedListeners;

class ExoPlayerTwoFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean RESET_STATE = false;

    private final SimpleExoPlayer exoPlayer;
    private final MediaSourceFactory mediaSourceFactory;
    private ExoPlayerForwarder forwarder;
    private int videoWidth;
    private int videoHeight;

    static ExoPlayerTwoFacade newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector, new DefaultLoadControl());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

        return new ExoPlayerTwoFacade(
                exoPlayer,
                mediaSourceFactory
        );
    }

    private ExoPlayerTwoFacade(SimpleExoPlayer exoPlayer,
                               MediaSourceFactory mediaSourceFactory) {
        this.exoPlayer = exoPlayer;
        this.mediaSourceFactory = mediaSourceFactory;
    }

    boolean getPlayWhenReady() {
        return exoPlayer.getPlayWhenReady();
    }

    long getPlayheadPosition() {
        return exoPlayer.getCurrentPosition();
    }

    int getVideoWidth() {
        return videoWidth;
    }

    int getVideoHeight() {
        return videoHeight;
    }

    long getMediaDuration() {
        return exoPlayer.getDuration();
    }

    int getBufferedPercentage() {
        return exoPlayer.getBufferedPercentage();
    }

    void seekTo(long positionMillis) {
        exoPlayer.seekTo(positionMillis);
    }

    void prepare(Uri uri, ContentType contentType) {
        exoPlayer.addListener(forwarder.exoPlayerEventListener());
        exoPlayer.setVideoDebugListener(forwarder.videoRendererEventListener());

        exoPlayer.setPlayWhenReady(true);

        MediaSource mediaSource = mediaSourceFactory.create(contentType, uri, forwarder.extractorMediaSourceListener(), forwarder.mediaSourceEventListener());
        exoPlayer.prepare(mediaSource, RESET_POSITION, RESET_STATE);
    }

    public void play() {
        exoPlayer.setPlayWhenReady(true);
    }

    void pause() {
        exoPlayer.setPlayWhenReady(false);
    }

    void stop() {
        exoPlayer.stop();
    }

    void release() {
        exoPlayer.release();
    }

    void setPlayer(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    void setForwarder(ExoPlayerForwarder forwarder) {
        this.forwarder = forwarder;
        forwarder.bind(new VideoSizeChangedListeners() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                ExoPlayerTwoFacade.this.videoWidth = width;
                ExoPlayerTwoFacade.this.videoHeight = height;
            }
        });
    }
}
