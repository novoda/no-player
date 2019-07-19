package com.novoda.noplayer.internal.exoplayer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.novoda.noplayer.model.PlayerVideoTrackCodecMapping;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Relaxes the Drm requirement so that a secure decoder is selected in the event that `DrmInitData` is present.
 * <p>
 * Also contains a workaround for sorting codecs which can be reverted once
 * https://github.com/google/ExoPlayer/blob/dev-v2/library/core/src/main/java/com/google/android/exoplayer2/video/MediaCodecVideoRenderer.java#L385
 * hits the release branch. See https://github.com/novoda/no-player/pull/265.
 */
class MediaCodecVideoRendererWithSimplifiedDrmRequirement extends MediaCodecVideoRenderer {

    private static final int LEVEL_FOUR = 4;
    private static final int LEVEL_EIGHT = 8;
    private static final int LEVEL_NINE = 9;
    private static final String TAG = MediaCodecVideoRendererWithSimplifiedDrmRequirement.class.getSimpleName();

    private final boolean requiresSecureDecoder;
    private final List<String> unsupportedVideoDecoders;

    // Extension from MediaCodecVideoRenderer, we can't do anything about this.
    @SuppressWarnings({"checkstyle:ParameterNumber", "PMD.ExcessiveParameterList"})
    MediaCodecVideoRendererWithSimplifiedDrmRequirement(Context context,
                                                        MediaCodecSelector mediaCodecSelector,
                                                        long allowedJoiningTimeMs,
                                                        @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                                        boolean playClearSamplesWithoutKeys,
                                                        boolean enableDecoderFallback,
                                                        boolean requiresSecureDecoder,
                                                        List<String> unsupportedVideoDecoders,
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
        this.requiresSecureDecoder = requiresSecureDecoder;
        this.unsupportedVideoDecoders = unsupportedVideoDecoders;
    }

    @Override
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector,
                                                   Format format,
                                                   boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return getDecoderInfos(mediaCodecSelector, format, requiresSecureDecoder(format), getCodecNeedsEosPropagation(), unsupportedVideoDecoders);
    }

    private boolean requiresSecureDecoder(Format format) {
        return format.drmInitData != null && requiresSecureDecoder;
    }

    @SuppressWarnings({"PMD.AvoidDeeplyNestedIfStmts"})
    private static List<MediaCodecInfo> getDecoderInfos(
            MediaCodecSelector mediaCodecSelector,
            Format format,
            boolean requiresSecureDecoder,
            boolean requiresTunnelingDecoder,
            List<String> unsupportedVideoDecoders)
            throws MediaCodecUtil.DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = mediaCodecSelector.getDecoderInfos(
                format.sampleMimeType,
                requiresSecureDecoder,
                requiresTunnelingDecoder
        );

        decoderInfos = InternalMediaCodecUtil.getOnlySupportedDecoderInfos(decoderInfos, format);

        if (MimeTypes.VIDEO_DOLBY_VISION.equals(format.sampleMimeType)) {
            // Fallback to primary decoders for H.265/HEVC or H.264/AVC for the relevant DV profiles.
            Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format.codecs);

            if (codecProfileAndLevel != null) {
                int profile = codecProfileAndLevel.first;
                if (profile == LEVEL_FOUR || profile == LEVEL_EIGHT) {
                    List<MediaCodecInfo> infos = mediaCodecSelector.getDecoderInfos(
                            MimeTypes.VIDEO_H265,
                            requiresSecureDecoder,
                            requiresTunnelingDecoder
                    );
                    decoderInfos.addAll(infos);
                } else if (profile == LEVEL_NINE) {
                    List<MediaCodecInfo> infos = mediaCodecSelector.getDecoderInfos(
                            MimeTypes.VIDEO_H264,
                            requiresSecureDecoder,
                            requiresTunnelingDecoder
                    );
                    decoderInfos.addAll(infos);
                }
            }
        }

        decoderInfos = InternalMediaCodecUtil.removeUnsupportedVideoDecoders(decoderInfos, unsupportedVideoDecoders);
        saveTrackCodecMapping(format.codecs, decoderInfos);

        printDecoderInfos(format.codecs, decoderInfos);

        return Collections.unmodifiableList(decoderInfos);
    }

    private static void saveTrackCodecMapping(String trackCodecName, List<MediaCodecInfo> decoderInfos) {
        new PlayerVideoTrackCodecMapping().addTrackCodec(trackCodecName, codecName(decoderInfos));
    }

    private static String codecName(List<MediaCodecInfo> decoderInfos) {
        return decoderInfos.isEmpty() ? "no codec available" : decoderInfos.get(0).name;

    }

    private static void printDecoderInfos(String codecs, List<MediaCodecInfo> decoderInfos) {
        Log.v(TAG, "track with codec: " + codecs + ", has " + decoderInfos.size() + " available decoders");
        for (MediaCodecInfo decoderInfo : decoderInfos) {
            Log.v(TAG, "- decoder name: " + decoderInfo.name);
        }
    }
}
