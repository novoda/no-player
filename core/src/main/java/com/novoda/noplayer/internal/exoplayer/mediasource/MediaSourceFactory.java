package com.novoda.noplayer.internal.exoplayer.mediasource;

import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
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
                              MediaSourceEventListener mediaSourceEventListener) {
        switch (contentType) {
            case HLS:
                return createHlsMediaSource(uri, mediaSourceEventListener);
            case H264:
                return createH264MediaSource(uri, mediaSourceEventListener);
            case DASH:
                return createDashMediaSource(uri);
            default:
                throw new UnsupportedOperationException("Content type: " + contentType + " is not supported.");
        }
    }

    private MediaSource createHlsMediaSource(Uri uri, MediaSourceEventListener mediaSourceEventListener) {
        HlsMediaSource.Factory factory = new HlsMediaSource.Factory(mediaDataSourceFactory);
        HlsMediaSource hlsMediaSource = factory.createMediaSource(uri);
        hlsMediaSource.addEventListener(handler, mediaSourceEventListener);
        return hlsMediaSource;
    }

    private MediaSource createH264MediaSource(Uri uri, MediaSourceEventListener mediaSourceEventListener) {
        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(mediaDataSourceFactory);
        ExtractorMediaSource extractorMediaSource = factory
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(uri);
        extractorMediaSource.addEventListener(handler, mediaSourceEventListener);
        return extractorMediaSource;
    }

    private MediaSource createDashMediaSource(Uri uri) {
        DefaultDashChunkSource.Factory chunkSourceFactory = new DefaultDashChunkSource.Factory(mediaDataSourceFactory);
        DashMediaSource.Factory factory = new DashMediaSource.Factory(chunkSourceFactory, mediaDataSourceFactory);
        return factory.createMediaSource(uri);
    }
}
