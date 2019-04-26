package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevelNotifier;
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
                                  DefaultDrmSessionEventListener drmSessionEventListener,
                                  MediaCodecSelector mediaCodecSelector,
                                  TrackSelector trackSelector) {
        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = drmSessionCreator.create(drmSessionEventListener, new DrmSecurityLevelNotifier() {
            @Override
            public void contentSecurityLevel(String securityLevel) {

            }
        });
        SubtitleDecoderFactory subtitleDecoderFactory = new NoPlayerSubtitleDecoderFactory();
        RenderersFactory renderersFactory = new SimpleRenderersFactory(
                context,
                EXTENSION_RENDERER_MODE_OFF,
                DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS,
                mediaCodecSelector,
                subtitleDecoderFactory
        );

        DefaultLoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl, drmSessionManager);
    }
}
