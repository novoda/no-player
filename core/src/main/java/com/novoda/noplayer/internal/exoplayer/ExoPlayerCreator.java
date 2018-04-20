package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.text.NoPlayerSubtitleDecoderFactory;

import static com.novoda.noplayer.internal.exoplayer.SimpleRenderersFactory.EXTENSION_RENDERER_MODE_OFF;

class ExoPlayerCreator {

    private static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;

    private final Context context;

    ExoPlayerCreator(Context context) {
        this.context = context;
    }

    @NonNull
    public SimpleExoPlayer create(DrmSessionCreator drmSessionCreator,
                                  DefaultDrmSessionManager.EventListener drmSessionEventListener,
                                  MediaCodecSelector mediaCodecSelector,
                                  CompositeTrackSelector trackSelector) {
        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = drmSessionCreator.create(drmSessionEventListener);
        SubtitleDecoderFactory subtitleDecoderFactory = new NoPlayerSubtitleDecoderFactory();
        RenderersFactory renderersFactory = new SimpleRenderersFactory(
                context,
                drmSessionManager,
                EXTENSION_RENDERER_MODE_OFF,
                DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS,
                mediaCodecSelector,
                subtitleDecoderFactory
        );

        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }
}
