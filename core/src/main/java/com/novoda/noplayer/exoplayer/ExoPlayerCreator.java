package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerDrmSessionEventListener;

class ExoPlayerCreator {

    private final Context context;
    private final DefaultTrackSelector trackSelector;

    ExoPlayerCreator(Context context, DefaultTrackSelector trackSelector) {
        this.context = context;
        this.trackSelector = trackSelector;
    }

    @NonNull
    public SimpleExoPlayer create(DrmSessionCreator drmSessionCreator, ExoPlayerDrmSessionEventListener exoPlayerDrmSessionEventListener) {
        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = drmSessionCreator.create(exoPlayerDrmSessionEventListener);
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context, drmSessionManager);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }
}
