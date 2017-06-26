package com.novoda.noplayer.exoplayer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.novoda.noplayer.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.forwarder.ExoPlayerDrmSessionEventListener;

import static com.novoda.noplayer.exoplayer.SimpleRenderersFactory.EXTENSION_RENDERER_MODE_OFF;

class ExoPlayerCreator {

    private static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;

    private final Context context;
    private final DefaultTrackSelector trackSelector;

    ExoPlayerCreator(Context context, DefaultTrackSelector trackSelector) {
        this.context = context;
        this.trackSelector = trackSelector;
    }

    @NonNull
    public SimpleExoPlayer create(DrmSessionCreator drmSessionCreator, ExoPlayerDrmSessionEventListener exoPlayerDrmSessionEventListener, boolean useSecureCodec) {
        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = drmSessionCreator.create(exoPlayerDrmSessionEventListener);
        RenderersFactory renderersFactory = new SimpleRenderersFactory(
                context,
                drmSessionManager,
                EXTENSION_RENDERER_MODE_OFF,
                DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS,
                new CodecSelector(useSecureCodec)
        );

        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }
}
