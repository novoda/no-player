package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.internal.listeners.PlayerListenersHolder;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerSubtitleTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.internal.exoplayer.mediasource.MediaSourceFactory;
import com.novoda.noplayer.model.LoadTimeout;

public class NoPlayerExoPlayerCreator {

    private final InternalCreator internalCreator;

    public static NoPlayerExoPlayerCreator newInstance(Handler handler) {
        InternalCreator internalCreator = new InternalCreator(handler);
        return new NoPlayerExoPlayerCreator(internalCreator);
    }

    NoPlayerExoPlayerCreator(InternalCreator internalCreator) {
        this.internalCreator = internalCreator;
    }

    public Player createExoPlayer(Context context, DrmSessionCreator drmSessionCreator, boolean downgradeSecureDecoder) {
        ExoPlayerTwoImpl player = internalCreator.create(context, drmSessionCreator, downgradeSecureDecoder);
        player.initialise();
        return player;
    }

    static class InternalCreator {

        private final Handler handler;

        InternalCreator(Handler handler) {
            this.handler = handler;
        }

        ExoPlayerTwoImpl create(Context context, DrmSessionCreator drmSessionCreator, boolean downgradeSecureDecoder) {
            DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
            MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();

            MediaCodecSelector mediaCodecSelector = downgradeSecureDecoder ? SecurityDowngradingCodecSelector.newInstance() : MediaCodecSelector.DEFAULT;

            ExoPlayerTrackSelector exoPlayerTrackSelector = ExoPlayerTrackSelector.newInstance(trackSelector);
            FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
            ExoPlayerAudioTrackSelector exoPlayerAudioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);
            ExoPlayerSubtitleTrackSelector exoPlayerSubtitleTrackSelector = new ExoPlayerSubtitleTrackSelector(
                    exoPlayerTrackSelector,
                    trackSelectionFactory
            );

            ExoPlayerCreator exoPlayerCreator = new ExoPlayerCreator(context, trackSelector);
            RendererTypeRequesterCreator rendererTypeRequesterCreator = new RendererTypeRequesterCreator();
            ExoPlayerFacade exoPlayerFacade = new ExoPlayerFacade(
                    mediaSourceFactory,
                    exoPlayerAudioTrackSelector,
                    exoPlayerSubtitleTrackSelector,
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
    }
}
