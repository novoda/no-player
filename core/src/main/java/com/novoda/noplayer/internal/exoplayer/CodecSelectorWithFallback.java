package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class will try and get a decoder that requires secure decryption and fallback
 * to a decoder that does not require secure decryption if there is none available.
 */
class CodecSelectorWithFallback implements MediaCodecSelector {

    private static final boolean DECODER_REQUIRES_SECURE_DECRYPTION = true;
    private static final boolean DECODER_DOES_NOT_REQUIRE_SECURE_DECRYPTION = false;

    private final InternalMediaCodecUtil internalMediaCodecUtil;
    private final CodecSecurityRequirement codecSecurityRequirement;

    public static CodecSelectorWithFallback newInstance(CodecSecurityRequirement codecSecurityRequirement) {
        InternalMediaCodecUtil internalMediaCodecUtil = new InternalMediaCodecUtil();
        return new CodecSelectorWithFallback(internalMediaCodecUtil, codecSecurityRequirement);
    }

    CodecSelectorWithFallback(InternalMediaCodecUtil internalMediaCodecUtil, CodecSecurityRequirement codecSecurityRequirement) {
        this.internalMediaCodecUtil = internalMediaCodecUtil;
        this.codecSecurityRequirement = codecSecurityRequirement;
    }

    @Override
    public List<MediaCodecInfo> getDecoderInfos(
            String mimeType,
            boolean requiresSecureDecoder,
            boolean requiresTunnelingDecoder
    ) throws MediaCodecUtil.DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = new ArrayList<>(secureCodecs(mimeType, requiresTunnelingDecoder));

        decoderInfos.addAll(
                internalMediaCodecUtil.getDecoderInfos(
                        mimeType,
                        DECODER_DOES_NOT_REQUIRE_SECURE_DECRYPTION,
                        requiresTunnelingDecoder
                )
        );

        return decoderInfos;
    }

    private List<MediaCodecInfo> secureCodecs(String mimeType, boolean requiresTunnelingDecoder) throws MediaCodecUtil.DecoderQueryException {
        if (codecSecurityRequirement.secureCodecsRequired()) {
            return internalMediaCodecUtil.getDecoderInfos(
                    mimeType,
                    DECODER_REQUIRES_SECURE_DECRYPTION,
                    requiresTunnelingDecoder
            );
        }
        return Collections.emptyList();
    }

    @Override
    public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getPassthroughDecoderInfo();
    }

    static class InternalMediaCodecUtil {

        List<MediaCodecInfo> getDecoderInfos(
                String mimeType,
                boolean requiresSecureDecoder,
                boolean requiresTunnelingDecoder
        ) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder);
        }

        MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }
}
