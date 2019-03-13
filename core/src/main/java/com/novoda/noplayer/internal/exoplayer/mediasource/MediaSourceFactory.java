package com.novoda.noplayer.internal.exoplayer.mediasource;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.internal.exoplayer.MyAdsLoader;
import com.novoda.noplayer.internal.utils.Optional;

public class MediaSourceFactory {

    private final Context context;
    private final Handler handler;
    private final Optional<DataSource.Factory> dataSourceFactory;
    private final String userAgent;
    private final Optional<MyAdsLoader> advertsLoader;
    private final boolean allowCrossProtocolRedirects;

    public MediaSourceFactory(Context context,
                              String userAgent,
                              Handler handler,
                              Optional<DataSource.Factory> dataSourceFactory,
                              Optional<MyAdsLoader> advertsLoader,
                              boolean allowCrossProtocolRedirects) {
        this.context = context;
        this.handler = handler;
        this.dataSourceFactory = dataSourceFactory;
        this.userAgent = userAgent;
        this.advertsLoader = advertsLoader;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
    }

    public MediaSource create(Options options,
                              Uri uri,
                              MediaSourceEventListener mediaSourceEventListener,
                              DefaultBandwidthMeter bandwidthMeter) {
        DefaultDataSourceFactory defaultDataSourceFactory = createDataSourceFactory(bandwidthMeter);

        MediaSource contentMediaSource;
        switch (options.contentType()) {
            case HLS:
                contentMediaSource = createHlsMediaSource(defaultDataSourceFactory, uri);
                break;
            case H264:
                contentMediaSource = createH264MediaSource(defaultDataSourceFactory, uri);
                break;
            case DASH:
                contentMediaSource = createDashMediaSource(defaultDataSourceFactory, uri);
                break;
            default:
                throw new UnsupportedOperationException("Content type: " + options + " is not supported.");
        }

        if (advertsLoader.isPresent()) {
            AdsMediaSource adsMediaSource = new AdsMediaSource(contentMediaSource, defaultDataSourceFactory, advertsLoader.get(), null);
            adsMediaSource.addEventListener(handler, mediaSourceEventListener);
            return adsMediaSource;
        } else {
            contentMediaSource.addEventListener(handler, mediaSourceEventListener);
            return contentMediaSource;
        }
    }

    private DefaultDataSourceFactory createDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        if (dataSourceFactory.isPresent()) {
            return new DefaultDataSourceFactory(context, bandwidthMeter, dataSourceFactory.get());
        } else {
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                    userAgent,
                    bandwidthMeter,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    allowCrossProtocolRedirects
            );

            return new DefaultDataSourceFactory(context, bandwidthMeter, httpDataSourceFactory);
        }
    }

    private MediaSource createHlsMediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                             Uri uri) {
        HlsMediaSource.Factory factory = new HlsMediaSource.Factory(defaultDataSourceFactory);
        return factory.createMediaSource(uri);
    }

    private MediaSource createH264MediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                              Uri uri) {
        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(defaultDataSourceFactory);
        return factory
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(uri);
    }

    private MediaSource createDashMediaSource(DefaultDataSourceFactory defaultDataSourceFactory,
                                              Uri uri) {
        DefaultDashChunkSource.Factory chunkSourceFactory = new DefaultDashChunkSource.Factory(defaultDataSourceFactory);
        DashMediaSource.Factory factory = new DashMediaSource.Factory(chunkSourceFactory, defaultDataSourceFactory);
        return factory.createMediaSource(uri);
    }
}
