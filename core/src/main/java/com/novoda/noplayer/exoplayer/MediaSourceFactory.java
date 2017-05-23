package com.novoda.noplayer.exoplayer;

import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.novoda.noplayer.ContentType;

class MediaSourceFactory {

    private final DataSource.Factory mediaDataSourceFactory;
    private final Handler handler;

    MediaSourceFactory(DataSource.Factory mediaDataSourceFactory, Handler handler) {
        this.mediaDataSourceFactory = mediaDataSourceFactory;
        this.handler = handler;
    }

    MediaSource create(ContentType contentType,
                       Uri uri,
                       ExtractorMediaSource.EventListener eventListener,
                       AdaptiveMediaSourceEventListener mediaSourceEventListener) {
        switch (contentType) {
            case HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, handler, mediaSourceEventListener);
            case DASH:
                return new ExtractorMediaSource(
                        uri,
                        mediaDataSourceFactory,
                        new DefaultExtractorsFactory(),
                        handler,
                        eventListener
                );
            case H264:
            default:
                throw new UnsupportedOperationException("Content type: " + contentType + " is not supported.");

        }
    }
}
