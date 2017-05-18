package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.SurfaceHolderRequester;

import java.io.IOException;

public class ExoPlayerTwoFacade implements VideoRendererEventListener {

    private int videoWidth;
    private int videoHeight;

    private final SimpleExoPlayer exoPlayer;
    private final DefaultTrackSelector trackSelector;
    private final MediaSourceFactory mediaSourceFactory;
    private SurfaceHolderRequester surfaceHolderRequester;

    public static ExoPlayerTwoFacade newInstance(Context context) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);
        return new ExoPlayerTwoFacade(exoPlayer, trackSelector, mediaSourceFactory);
    }

    public ExoPlayerTwoFacade(SimpleExoPlayer exoPlayer,
                              DefaultTrackSelector trackSelector,
                              MediaSourceFactory mediaSourceFactory) {
        this.exoPlayer = exoPlayer;
        this.trackSelector = trackSelector;
        this.mediaSourceFactory = mediaSourceFactory;
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

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoHeight = height;
        videoWidth = width;
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

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
            if (playWhenReady) {
                // TODO do pushSurface (what is it?)
            }
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    public void seekTo(long positionMillis) {
        if (hasPlayer()) {
            exoPlayer.seekTo(positionMillis);
        }
    }

    public void release() {
        exoPlayer.release();
    }

    public void prepare(Uri uri, ContentType contentType) {

        EventLogger eventLogger = new EventLogger(trackSelector);
        exoPlayer.addListener(eventLogger);
        exoPlayer.setAudioDebugListener(eventLogger);
        exoPlayer.setVideoDebugListener(eventLogger);
        exoPlayer.setMetadataOutput(eventLogger);
        setPlayWhenReady(true);

        MediaSource mediaSource = mediaSourceFactory.create(contentType, uri, eventListener);
        exoPlayer.prepare(mediaSource, true, false);
    }

    private final ExtractorMediaSource.EventListener eventListener = new ExtractorMediaSource.EventListener() {
        @Override
        public void onLoadError(IOException error) {
            Log.e("!!!", "ON LOAD ERROR");
        }
    };

    public void setSurfaceHolderRequester(SurfaceHolderRequester surfaceHolderRequester) {
        this.surfaceHolderRequester = surfaceHolderRequester;
    }

    public void setPlayer(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    static class MediaSourceFactory {

        private final DataSource.Factory mediaDataSourceFactory;
        private final Handler handler;

        MediaSourceFactory(DataSource.Factory mediaDataSourceFactory, Handler handler) {
            this.mediaDataSourceFactory = mediaDataSourceFactory;
            this.handler = handler;
        }

        MediaSource create(ContentType contentType, Uri uri, ExtractorMediaSource.EventListener eventListener) {
            switch (contentType) {
                case HLS:
                    return new HlsMediaSource(uri, mediaDataSourceFactory, handler, this.eventListener);
                case DASH:
                case H264:
                default:
                    return new ExtractorMediaSource(
                            uri,
                            mediaDataSourceFactory,
                            new DefaultExtractorsFactory(),
                            handler,
                            eventListener
                    );

            }
        }

        private final AdaptiveMediaSourceEventListener eventListener = new AdaptiveMediaSourceEventListener() {
            @Override
            public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
                    trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
                // no-op
            }

            @Override
            public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
                    trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
                // no-op
            }

            @Override
            public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
                    trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
                // no-op
            }

            @Override
            public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object
                    trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException
                                            error, boolean wasCanceled) {
                // no-op
            }

            @Override
            public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
                // no-op
            }

            @Override
            public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData,
                                                  long mediaTimeMs) {
                // no-op
            }
        };
    }
}
