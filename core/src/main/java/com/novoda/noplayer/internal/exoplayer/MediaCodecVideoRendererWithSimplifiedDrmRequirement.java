package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

import androidx.annotation.Nullable;

class MediaCodecVideoRendererWithSimplifiedDrmRequirement extends MediaCodecVideoRenderer {

    // Extension from MediaCodecVideoRenderer, we can't do anything about this.
    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
    MediaCodecVideoRendererWithSimplifiedDrmRequirement(Context context,
                                                        MediaCodecSelector mediaCodecSelector,
                                                        long allowedJoiningTimeMs,
                                                        @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                                        boolean playClearSamplesWithoutKeys,
                                                        boolean enableDecoderFallback,
                                                        @Nullable Handler eventHandler,
                                                        @Nullable VideoRendererEventListener eventListener,
                                                        int maxDroppedFramesToNotify) {
        super(
                context,
                mediaCodecSelector,
                allowedJoiningTimeMs,
                drmSessionManager,
                playClearSamplesWithoutKeys,
                enableDecoderFallback,
                eventHandler,
                eventListener,
                maxDroppedFramesToNotify
        );
    }

    @Override
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector,
                                                   Format format,
                                                   boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return super.getDecoderInfos(mediaCodecSelector, format, format.drmInitData != null);
    }
}
