package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.novoda.noplayer.text.NoPlayerSubtitleDecoderFactory;

import androidx.annotation.NonNull;

import static com.novoda.noplayer.internal.exoplayer.SimpleRenderersFactory.EXTENSION_RENDERER_MODE_OFF;

class ExoPlayerCreator {

    private static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;

    private final Context context;

    ExoPlayerCreator(Context context) {
        this.context = context;
    }

    @NonNull
    public SimpleExoPlayer create(DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                  boolean allowFallbackDecoder,
                                  TrackSelector trackSelector) {
        SubtitleDecoderFactory subtitleDecoderFactory = new NoPlayerSubtitleDecoderFactory();
        RenderersFactory renderersFactory = new SimpleRenderersFactory(
                context,
                EXTENSION_RENDERER_MODE_OFF,
                DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS,
                allowFallbackDecoder,
                subtitleDecoderFactory
        );

        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl, drmSessionManager);
    }
}
