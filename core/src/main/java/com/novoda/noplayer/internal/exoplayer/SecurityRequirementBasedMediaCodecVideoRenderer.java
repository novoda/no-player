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

class SecurityRequirementBasedMediaCodecVideoRenderer extends MediaCodecVideoRenderer {

    private final CodecSecurityRequirement codecSecurityRequirement;

    // Extension from MediaCodecVideoRenderer, we can't do anything about this.
    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
    SecurityRequirementBasedMediaCodecVideoRenderer(CodecSecurityRequirement codecSecurityRequirement,
                                                    Context context,
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
        this.codecSecurityRequirement = codecSecurityRequirement;
    }

    @Override
    protected int supportsFormat(MediaCodecSelector mediaCodecSelector,
                                 DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                 Format format) throws MediaCodecUtil.DecoderQueryException {
        codecSecurityRequirement.updateSecureCodecsRequirement(format.drmInitData);
        return super.supportsFormat(mediaCodecSelector, drmSessionManager, format);
    }

    @Override
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector,
                                                   Format format,
                                                   boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        codecSecurityRequirement.updateSecureCodecsRequirement(format.drmInitData);
        return super.getDecoderInfos(mediaCodecSelector, format, requiresSecureDecoder);
    }
}
