package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.novoda.noplayer.ContentType;

import java.io.IOException;

class MediaSourceFactory {

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
