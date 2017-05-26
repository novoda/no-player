package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.ContentType;

class ExoPlayerTwoFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean RESET_STATE = false;

    private final SimpleExoPlayer exoPlayer;
    private final MediaSourceFactory mediaSourceFactory;
    private ExoForwarder forwarder;

    static ExoPlayerTwoFacade newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());
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
        if (hasPlayer()) {
            return exoPlayer.getPlayWhenReady();
        } else {
            return false;
        }
    }

    long getPlayheadPosition() {
        if (hasPlayer()) {
            return exoPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    private boolean hasPlayer() {
        return exoPlayer != null;
    }

    int getVideoWidth() {
        return forwarder.videoRendererEventListener().videoWidth();
    }

    int getVideoHeight() {
        return forwarder.videoRendererEventListener().videoHeight();
    }

    long getMediaDuration() {
        if (hasPlayer()) {
            return exoPlayer.getDuration();
        } else {
            return 0;
        }
    }

    int getBufferedPercentage() {
        if (hasPlayer()) {
            return exoPlayer.getBufferedPercentage();
        } else {
            return 0;
        }
    }

    void setPlayWhenReady(boolean playWhenReady) {
        if (hasPlayer()) {
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    void seekTo(long positionMillis) {
        if (hasPlayer()) {
            exoPlayer.seekTo(positionMillis);
        }
    }

    void release() {
        if (hasPlayer()) {
            exoPlayer.release();
        }
    }

    void prepare(Uri uri, ContentType contentType) {
        if (hasPlayer()) {
            exoPlayer.addListener(forwarder.exoPlayerEventListener());
            exoPlayer.setVideoDebugListener(forwarder.videoRendererEventListener());

            setPlayWhenReady(true);

            MediaSource mediaSource = mediaSourceFactory.create(contentType, uri, forwarder.mediaSourceListener(), forwarder.mediaSourceEventListener());
            exoPlayer.prepare(mediaSource, RESET_POSITION, RESET_STATE);
        }
    }

    void setPlayer(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    void stop() {
        if (hasPlayer()) {
            exoPlayer.stop();
        }
    }

    void setForwarder(ExoForwarder forwarder) {
        this.forwarder = forwarder;
    }
}
