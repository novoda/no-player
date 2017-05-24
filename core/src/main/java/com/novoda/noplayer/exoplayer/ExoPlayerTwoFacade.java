package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.ContentType;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExoPlayerTwoFacade implements VideoRendererEventListener {

    private static final boolean RESET_POSITION = true;
    private static final boolean RESET_STATE = false;

    private int videoWidth;
    private int videoHeight;

    private final SimpleExoPlayer exoPlayer;
    private final MediaSourceFactory mediaSourceFactory;
    private final EventListener exoPlayerEventListener;
    private final MediaSourceEventListener mediaSourceEventListener;
    private final List<Forwarder> forwarders;

    private InternalErrorListener internalErrorListener;

    public static ExoPlayerTwoFacade newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

        List<Forwarder> forwarders = new CopyOnWriteArrayList<>();
        EventListener exoPlayerEventListener = new EventListener(forwarders);
        MediaSourceEventListener mediaSourceEventListener = new MediaSourceEventListener(forwarders);

        return new ExoPlayerTwoFacade(exoPlayer, mediaSourceFactory, forwarders, exoPlayerEventListener, mediaSourceEventListener);
    }

    public ExoPlayerTwoFacade(SimpleExoPlayer exoPlayer,
                              MediaSourceFactory mediaSourceFactory,
                              List<Forwarder> forwarders,
                              EventListener exoPlayerEventListener,
                              MediaSourceEventListener mediaSourceEventListener) {
        this.exoPlayer = exoPlayer;
        this.mediaSourceFactory = mediaSourceFactory;
        this.exoPlayerEventListener = exoPlayerEventListener;
        this.forwarders = forwarders;
        this.mediaSourceEventListener = mediaSourceEventListener;
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
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public long getMediaDuration() {
        if (hasPlayer()) {
            return exoPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        // no-op
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        // no-op
    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        // no-op
    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        for (Forwarder forwarder : forwarders) {
            forwarder.onDroppedFrames(count, elapsedMs);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoHeight = height;
        videoWidth = width;
        for (Forwarder forwarder : forwarders) {
            forwarder.forwardVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        // no-op
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        // no-op
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

            MediaSource mediaSource = mediaSourceFactory.create(contentType, uri, extractorMediaSourceEventListener, mediaSourceEventListener);
            exoPlayer.prepare(mediaSource, RESET_POSITION, RESET_STATE);
        }
    }

    private final ExtractorMediaSource.EventListener extractorMediaSourceEventListener = new ExtractorMediaSource.EventListener() {
        @Override
        public void onLoadError(IOException error) {
            internalErrorListener.onLoadError(error);
        }
    };

    ;

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

    public void setInternalErrorListener(InternalErrorListener internalErrorListener) {
        this.internalErrorListener = internalErrorListener;
    }

}
