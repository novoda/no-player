package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.upstream.DataSource;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.SystemClock;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerListener;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.LoadTimeout;

public class NoPlayerExoPlayerCreator {

    private final InternalCreator internalCreator;

    public static NoPlayerExoPlayerCreator newInstance(String userAgent, Handler handler) {
        InternalCreator internalCreator = new InternalCreator(
                userAgent,
                handler,
                Optional.<DataSource.Factory>absent(),
                Optional.<AdvertsLoader>absent()
        );
        return new NoPlayerExoPlayerCreator(internalCreator);
    }

    public static NoPlayerExoPlayerCreator newInstance(String userAgent, Handler handler, AdvertsLoader advertsLoader) {
        InternalCreator internalCreator = new InternalCreator(
                userAgent,
                handler,
                Optional.<DataSource.Factory>absent(),
                Optional.<AdvertsLoader>of(advertsLoader)
        );
        return new NoPlayerExoPlayerCreator(internalCreator);
    }

    public static NoPlayerExoPlayerCreator newInstance(String userAgent, Handler handler, DataSource.Factory dataSourceFactory) {
        InternalCreator internalCreator = new InternalCreator(userAgent, handler, Optional.of(dataSourceFactory), Optional.<AdvertsLoader>absent());
        return new NoPlayerExoPlayerCreator(internalCreator);
    }

    NoPlayerExoPlayerCreator(InternalCreator internalCreator) {
        this.internalCreator = internalCreator;
    }

    public NoPlayer createExoPlayer(Context context,
                                    DrmSessionCreator drmSessionCreator,
                                    boolean downgradeSecureDecoder,
                                    boolean allowCrossProtocolRedirects) {
        ExoPlayerTwoImpl player = internalCreator.create(context, drmSessionCreator, downgradeSecureDecoder, allowCrossProtocolRedirects);
        player.initialise();
        return player;
    }

    static class InternalCreator {

        private final Handler handler;
        private final Optional<DataSource.Factory> dataSourceFactory;
        private final Optional<AdvertsLoader> advertsLoader;
        private final String userAgent;

        InternalCreator(String userAgent, Handler handler, Optional<DataSource.Factory> dataSourceFactory, Optional<AdvertsLoader> advertsLoader) {
            this.userAgent = userAgent;
            this.handler = handler;
            this.dataSourceFactory = dataSourceFactory;
            this.advertsLoader = advertsLoader;
        }

        ExoPlayerTwoImpl create(Context context,
                                DrmSessionCreator drmSessionCreator,
                                boolean downgradeSecureDecoder,
                                boolean allowCrossProtocolRedirects) {
            Optional<AdsLoader> adsLoader = createAdsLoaderFrom(advertsLoader);

            MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(
                    context,
                    userAgent,
                    handler,
                    dataSourceFactory,
                    allowCrossProtocolRedirects
            );

            MediaCodecSelector mediaCodecSelector = downgradeSecureDecoder
                    ? SecurityDowngradingCodecSelector.newInstance()
                    : MediaCodecSelector.DEFAULT_WITH_FALLBACK;

            CompositeTrackSelectorCreator trackSelectorCreator = new CompositeTrackSelectorCreator();

            ExoPlayerCreator exoPlayerCreator = new ExoPlayerCreator(context);
            RendererTypeRequesterCreator rendererTypeRequesterCreator = new RendererTypeRequesterCreator();
            AndroidDeviceVersion androidDeviceVersion = AndroidDeviceVersion.newInstance();
            BandwidthMeterCreator bandwidthMeterCreator = new BandwidthMeterCreator(context);
            ExoPlayerFacade exoPlayerFacade = new ExoPlayerFacade(
                    bandwidthMeterCreator,
                    androidDeviceVersion,
                    mediaSourceFactory,
                    trackSelectorCreator,
                    exoPlayerCreator,
                    rendererTypeRequesterCreator,
                    adsLoader
            );

            PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
            ExoPlayerListener exoPlayerListener = new ExoPlayerListener();
            LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), handler);
            Heart heart = Heart.newInstance(handler);

            return new ExoPlayerTwoImpl(
                    exoPlayerFacade,
                    listenersHolder,
                    exoPlayerListener,
                    loadTimeout,
                    heart,
                    drmSessionCreator,
                    mediaCodecSelector
            );
        }

        private Optional<AdsLoader> createAdsLoaderFrom(Optional<AdvertsLoader> advertsLoader) {
            if (advertsLoader.isPresent()) {
                AdvertsLoader loader = advertsLoader.get();
                AdsLoader adsLoader = new NoPlayerAdsLoader(loader);
                return Optional.of(adsLoader);
            } else {
                return Optional.absent();
            }
        }

    }

}
