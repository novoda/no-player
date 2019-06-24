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
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Relaxes the Drm requirement so that a secure decoder is selected in the event that `DrmInitData` is present.
 * <p>
 * Also contains a workaround for sorting codecs which can be reverted once
 * https://github.com/google/ExoPlayer/blob/dev-v2/library/core/src/main/java/com/google/android/exoplayer2/audio/MediaCodecAudioRenderer.java#L342-L364
 * hits the release branch. See https://github.com/novoda/no-player/pull/265.
 */
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
        if (allowPassthrough(format.channelCount, format.sampleMimeType)) {
            MediaCodecInfo passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo();
            if (passthroughDecoderInfo != null) {
                return Collections.singletonList(passthroughDecoderInfo);
            }
        }
        List<MediaCodecInfo> decoderInfos =
                mediaCodecSelector.getDecoderInfos(
                        format.sampleMimeType, format.drmInitData != null, /* requiresTunnelingDecoder= */ false);
        decoderInfos = InternalMediaCodecUtil.getDecoderInfosSortedByFormatSupport(decoderInfos, format);
        if (MimeTypes.AUDIO_E_AC3_JOC.equals(format.sampleMimeType)) {
            // E-AC3 decoders can decode JOC streams, but in 2-D rather than 3-D.
            List<MediaCodecInfo> eac3DecoderInfos =
                    mediaCodecSelector.getDecoderInfos(
                            MimeTypes.AUDIO_E_AC3, format.drmInitData != null, /* requiresTunnelingDecoder= */ false);
            decoderInfos.addAll(eac3DecoderInfos);
        }
        return Collections.unmodifiableList(decoderInfos);
    }
}
