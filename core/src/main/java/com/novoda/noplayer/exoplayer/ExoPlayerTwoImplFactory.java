package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.novoda.noplayer.Heart;
import com.novoda.noplayer.LoadTimeout;
import com.novoda.noplayer.PlayerListenersHolder;
import com.novoda.noplayer.SystemClock;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerForwarder;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerAudioTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.ExoPlayerTrackSelector;
import com.novoda.noplayer.exoplayer.mediasource.MediaSourceFactory;

public class ExoPlayerTwoImplFactory {

    public ExoPlayerTwoImpl create(Context context, DrmSessionCreator drmSessionCreator) {
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "user-agent");
        Handler handler = new Handler(Looper.getMainLooper());
        MediaSourceFactory mediaSourceFactory = new MediaSourceFactory(defaultDataSourceFactory, handler);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        ExoPlayerTrackSelector exoPlayerTrackSelector = new ExoPlayerTrackSelector(trackSelector);
        FixedTrackSelection.Factory trackSelectionFactory = new FixedTrackSelection.Factory();
        ExoPlayerAudioTrackSelector exoPlayerAudioTrackSelector = new ExoPlayerAudioTrackSelector(exoPlayerTrackSelector, trackSelectionFactory);

        ExoPlayerCreator exoPlayerCreator = new ExoPlayerCreator(context, trackSelector);
        ExoPlayerFacade exoPlayerFacade = new ExoPlayerFacade(mediaSourceFactory, exoPlayerAudioTrackSelector, exoPlayerCreator);

        PlayerListenersHolder listenersHolder = new PlayerListenersHolder();
        ExoPlayerForwarder exoPlayerForwarder = new ExoPlayerForwarder();
        LoadTimeout loadTimeout = new LoadTimeout(new SystemClock(), new Handler(Looper.getMainLooper()));
        Heart heart = Heart.newInstance();

        return new ExoPlayerTwoImpl(
                exoPlayerFacade,
                listenersHolder,
                exoPlayerForwarder,
                loadTimeout,
                heart,
                drmSessionCreator
        );
    }
}
