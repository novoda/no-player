package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.List;

import androidx.annotation.Nullable;

class MediaCodecAudioRendererWithSimplifiedDrmRequirement extends MediaCodecAudioRenderer {

    // Extension from MediaCodecAudioRenderer, we can't do anything about this.
    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
    MediaCodecAudioRendererWithSimplifiedDrmRequirement(Context context,
                                                        MediaCodecSelector mediaCodecSelector,
                                                        @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                                        boolean playClearSamplesWithoutKeys,
                                                        @Nullable Handler eventHandler,
                                                        @Nullable AudioRendererEventListener eventListener,
                                                        @Nullable AudioCapabilities audioCapabilities,
                                                        AudioProcessor... audioProcessors) {
        super(
                context,
                mediaCodecSelector,
                drmSessionManager,
                playClearSamplesWithoutKeys,
                eventHandler,
                eventListener,
                audioCapabilities,
                audioProcessors
        );
    }

    @Override
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector,
                                                   Format format,
                                                   boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return super.getDecoderInfos(mediaCodecSelector, format, format.drmInitData != null);
    }
}
