package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.upstream.DataSource;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertsLoader;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.internal.SystemClock;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.LoadTimeout;

import java.io.IOException;
import java.util.List;

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
                    adsLoader,
                    allowCrossProtocolRedirects
            );

            MediaCodecSelector mediaCodecSelector = downgradeSecureDecoder
                    ? SecurityDowngradingCodecSelector.newInstance()
                    : MediaCodecSelector.DEFAULT;

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
                    rendererTypeRequesterCreator
            );

            PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
            ExoPlayerForwarder exoPlayerForwarder = new ExoPlayerForwarder();
            LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), handler);
            Heart heart = Heart.newInstance(handler);

            return new ExoPlayerTwoImpl(
                    exoPlayerFacade,
                    listenersHolder,
                    exoPlayerForwarder,
                    loadTimeout,
                    heart,
                    drmSessionCreator,
                    mediaCodecSelector
            );
        }

        private Optional<AdsLoader> createAdsLoaderFrom(final Optional<AdvertsLoader> advertsLoader) {
            if (advertsLoader.isPresent()) {
                final AdvertsLoader loader = advertsLoader.get();
                AdsLoader adsLoader = new AdsLoader() {
                    @Nullable
                    private AdPlaybackState adPlaybackState;

                    @Override
                    public void setSupportedContentTypes(int... contentTypes) {
                        for (int contentType : contentTypes) {
                            Log.e("LOADER", "setSupportedContentTypes: " + contentType);
                        }
                    }

                    @Override
                    public void start(final EventListener eventListener, AdViewProvider adViewProvider) {
                        loader.load(new AdvertsLoader.Callback() {
                            @Override
                            public void onAdvertsLoaded(List<AdvertBreak> advertBreaks) {
                                long[] advertOffsets = getAdvertOffsets(advertBreaks);
                                adPlaybackState = new AdPlaybackState(advertOffsets);
                                long[][] advertBreaksWithAdvertDurations = getAdvertBreakDurations(advertBreaks);
                                adPlaybackState = adPlaybackState.withAdDurationsUs(advertBreaksWithAdvertDurations);

                                for (int i = 0; i < advertBreaks.size(); i++) {
                                    List<Advert> adverts = advertBreaks.get(i).adverts();

                                    adPlaybackState = adPlaybackState.withAdCount(i, adverts.size());

                                    for (int j = 0; j < adverts.size(); j++) {
                                        Advert advert = adverts.get(j);
                                        adPlaybackState = adPlaybackState.withAdUri(i, j, advert.uri());
                                    }
                                }

                                Log.e("LOADER", "retrieved adverts");

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("LOADER", "send to main thread");
                                        updateAdPlaybackState();
                                    }
                                });
                            }

                            private void updateAdPlaybackState() {
                                if (eventListener != null) {
                                    Log.e("LOADER", "playback state: " + adPlaybackState);
                                    eventListener.onAdPlaybackState(adPlaybackState);
                                }
                            }

                            @Override
                            public void onAdvertsError(String message) {
                                eventListener.onAdLoadError(null, null);
                                Log.e("LOADER", "fail: " + message);
                            }
                        });
                    }

                    @Override
                    public void stop() {
                        Log.e("LOADER", "stop");
                    }

                    @Override
                    public void setPlayer(@Nullable Player player) {
                        Log.e("LOADER", "setPlayer");
                    }

                    @Override
                    public void release() {
                        Log.e("LOADER", "release");
                    }

                    @Override
                    public void handlePrepareError(int adGroupIndex, int adIndexInAdGroup, IOException exception) {
                        if (adPlaybackState != null) {
                            adPlaybackState = adPlaybackState.withAdLoadError(adGroupIndex, adIndexInAdGroup);
                        }
                    }
                };
                return Optional.of(adsLoader);
            } else {
                return Optional.absent();
            }
        }
    }

    private static long[] getAdvertOffsets(List<AdvertBreak> advertBreaks) {
        long[] advertOffsets = new long[advertBreaks.size()];
        for (int i = 0; i < advertOffsets.length; i++) {
            advertOffsets[i] = advertBreaks.get(i).startTime();
        }
        return advertOffsets;
    }

    private static long[][] getAdvertBreakDurations(List<AdvertBreak> advertBreaks) {
        long[][] advertBreaksWithAdvertDurations = new long[advertBreaks.size()][];
        for (int i = 0; i < advertBreaks.size(); i++) {
            AdvertBreak advertBreak = advertBreaks.get(i);
            List<Advert> adverts = advertBreak.adverts();
            long[] advertDurations = new long[adverts.size()];

            for (int j = 0; j < adverts.size(); j++) {
                advertDurations[j] = adverts.get(j).durationInMicros();
            }
            advertBreaksWithAdvertDurations[i] = advertDurations;
        }
        return advertBreaksWithAdvertDurations;
    }
}
