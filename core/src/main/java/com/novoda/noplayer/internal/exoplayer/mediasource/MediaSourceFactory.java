package com.novoda.noplayer.internal.exoplayer.mediasource;

import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.novoda.noplayer.ContentType;

public class MediaSourceFactory {

    private final DataSource.Factory mediaDataSourceFactory;
    private final Handler handler;

    public MediaSourceFactory(DataSource.Factory mediaDataSourceFactory, Handler handler) {
        this.mediaDataSourceFactory = mediaDataSourceFactory;
        this.handler = handler;
    }

    public MediaSource create(ContentType contentType,
                              Uri uri,
                              ExtractorMediaSource.EventListener eventListener,
                              AdaptiveMediaSourceEventListener mediaSourceEventListener) {
        switch (contentType) {
            case HLS:
                return new HlsMediaSource(
                        uri,
                        mediaDataSourceFactory,
                        handler,
                        mediaSourceEventListener
                );
            case H264:
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                return new ExtractorMediaSource(
                        uri,
                        mediaDataSourceFactory,
                        extractorsFactory,
                        handler,
                        eventListener
                );
            case DASH:
                DefaultDashChunkSource.Factory chunkSourceFactory = new DefaultDashChunkSource.Factory(mediaDataSourceFactory);
                return new DashMediaSource(
                        uri,
                        mediaDataSourceFactory,
                        chunkSourceFactory,
                        handler,
                        mediaSourceEventListener
                );
            default:
                throw new UnsupportedOperationException("Content type: " + contentType + " is not supported.");
        }
    }
}
