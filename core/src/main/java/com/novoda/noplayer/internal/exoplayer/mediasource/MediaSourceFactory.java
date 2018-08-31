package com.novoda.noplayer.internal.exoplayer.mediasource;

import android.content.Context;
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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.internal.utils.Optional;

public class MediaSourceFactory {

    private final Context context;
    private final Handler handler;
    private final Optional<DataSource.Factory> dataSourceFactory;

    public MediaSourceFactory(Context context, Handler handler, Optional<DataSource.Factory> dataSourceFactory) {
        this.context = context;
        this.handler = handler;
        this.dataSourceFactory = dataSourceFactory;
    }

    public MediaSource create(Options options,
                              Uri uri,
                              MediaSourceEventListener mediaSourceEventListener,
                              DefaultBandwidthMeter bandwidthMeter) {
        DefaultDataSourceFactory defaultDataSourceFactory = createDataSourceFactory(bandwidthMeter);
        switch (options.contentType()) {
            case HLS:
                return createHlsMediaSource(defaultDataSourceFactory, uri, mediaSourceEventListener);
            case H264:
                return createH264MediaSource(defaultDataSourceFactory, uri, mediaSourceEventListener);
            case DASH:
                return createDashMediaSource(defaultDataSourceFactory, uri, mediaSourceEventListener);
            default:
                throw new UnsupportedOperationException("Content type: " + options + " is not supported.");
        }
    }

    private DefaultDataSourceFactory createDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        if (dataSourceFactory.isPresent()) {
            return new DefaultDataSourceFactory(context, bandwidthMeter, dataSourceFactory.get());
        } else {
            return new DefaultDataSourceFactory(context, "user-agent", bandwidthMeter);
        }
    }

    private MediaSource createHlsMediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                             Uri uri,
                                             MediaSourceEventListener mediaSourceEventListener) {
        HlsMediaSource.Factory factory = new HlsMediaSource.Factory(defaultDataSourceFactory);
        HlsMediaSource hlsMediaSource = factory.createMediaSource(uri);
        hlsMediaSource.addEventListener(handler, mediaSourceEventListener);
        return hlsMediaSource;
    }

    private MediaSource createH264MediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                              Uri uri,
                                              MediaSourceEventListener mediaSourceEventListener) {
        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(defaultDataSourceFactory);
        ExtractorMediaSource extractorMediaSource = factory
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(uri);
        extractorMediaSource.addEventListener(handler, mediaSourceEventListener);
        return extractorMediaSource;
    }

    private MediaSource createDashMediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                              Uri uri,
                                              MediaSourceEventListener mediaSourceEventListener) {
        DefaultDashChunkSource.Factory chunkSourceFactory = new DefaultDashChunkSource.Factory(defaultDataSourceFactory);
        DashMediaSource.Factory factory = new DashMediaSource.Factory(chunkSourceFactory, defaultDataSourceFactory);
        DashMediaSource mediaSource = factory.createMediaSource(uri);
        mediaSource.addEventListener(handler, mediaSourceEventListener);
        return mediaSource;
    }
}
