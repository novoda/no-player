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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExoPlayerTwoFacade {

    private static final boolean RESET_POSITION = true;
    private static final boolean RESET_STATE = false;

    private final SimpleExoPlayer exoPlayer;
    private final MediaSourceFactory mediaSourceFactory;
    private final EventListener exoPlayerEventListener;
    private final MediaSourceEventListener mediaSourceEventListener;
    private final ExoPlayerVideoRendererEventListener videoRendererEventListener;
    private final ExoPlayerExtractorMediaSourceListener exoPlayerExtractorMediaSourceListener;
    private final List<Forwarder> forwarders;

    public static ExoPlayerTwoFacade newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

        List<Forwarder> forwarders = new CopyOnWriteArrayList<>();
        EventListener exoPlayerEventListener = new EventListener(forwarders);
        MediaSourceEventListener mediaSourceEventListener = new MediaSourceEventListener(forwarders);
        ExoPlayerVideoRendererEventListener videoRendererEventListener = new ExoPlayerVideoRendererEventListener(forwarders);
        ExoPlayerExtractorMediaSourceListener exoPlayerExtractorMediaSourceListener = new ExoPlayerExtractorMediaSourceListener(forwarders);

        return new ExoPlayerTwoFacade(
                exoPlayer,
                mediaSourceFactory,
                forwarders,
                exoPlayerEventListener,
                mediaSourceEventListener,
                videoRendererEventListener,
                exoPlayerExtractorMediaSourceListener
        );
    }

    public ExoPlayerTwoFacade(SimpleExoPlayer exoPlayer,
                              MediaSourceFactory mediaSourceFactory,
                              List<Forwarder> forwarders,
                              EventListener exoPlayerEventListener,
                              MediaSourceEventListener mediaSourceEventListener,
                              ExoPlayerVideoRendererEventListener videoRendererEventListener,
                              ExoPlayerExtractorMediaSourceListener exoPlayerExtractorMediaSourceListener) {
        this.exoPlayer = exoPlayer;
        this.mediaSourceFactory = mediaSourceFactory;
        this.exoPlayerEventListener = exoPlayerEventListener;
        this.forwarders = forwarders;
        this.mediaSourceEventListener = mediaSourceEventListener;
        this.videoRendererEventListener = videoRendererEventListener;
        this.exoPlayerExtractorMediaSourceListener = exoPlayerExtractorMediaSourceListener;
    }

    public boolean getPlayWhenReady() {
        if (hasPlayer()) {
            return exoPlayer.getPlayWhenReady();
        } else {
            return false;
        }
    }

    public long getPlayheadPosition() {
        if (hasPlayer()) {
            return exoPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    private boolean hasPlayer() {
        return exoPlayer != null;
    }

    public int getVideoWidth() {
        return videoRendererEventListener.videoWidth();
    }

    public int getVideoHeight() {
        return videoRendererEventListener.videoHeight();
    }

    public long getMediaDuration() {
        if (hasPlayer()) {
            return exoPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public int getBufferedPercentage() {
        if (hasPlayer()) {
            return exoPlayer.getBufferedPercentage();
        } else {
            return 0;
        }
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        if (hasPlayer()) {
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    public void seekTo(long positionMillis) {
        if (hasPlayer()) {
            exoPlayer.seekTo(positionMillis);
        }
    }

    public void release() {
        if (hasPlayer()) {
            exoPlayer.release();
        }
    }

    public void prepare(Uri uri, ContentType contentType) {
        if (hasPlayer()) {
            exoPlayer.addListener(exoPlayerEventListener);

            setPlayWhenReady(true);

            MediaSource mediaSource = mediaSourceFactory.create(contentType, uri, exoPlayerExtractorMediaSourceListener, mediaSourceEventListener);
            exoPlayer.prepare(mediaSource, RESET_POSITION, RESET_STATE);
        }
    }

    public void setPlayer(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    void addForwarder(Forwarder forwarder) {
        forwarders.add(forwarder);
    }

    public void stop() {
        if (hasPlayer()) {
            exoPlayer.stop();
        }
    }

}
