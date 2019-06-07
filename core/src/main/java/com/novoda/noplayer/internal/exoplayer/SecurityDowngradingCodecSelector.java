package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will try and get a decoder that requires secure decryption and fallback
 * to a decoder that does not require secure decryption if there is none available.
 */
class SecurityDowngradingCodecSelector implements MediaCodecSelector {

    private static final boolean DECODER_REQUIRES_SECURE_DECRYPTION = true;
    private static final boolean DECODER_DOES_NOT_REQUIRE_SECURE_DECRYPTION = false;

    private final InternalMediaCodecUtil internalMediaCodecUtil;

    public static SecurityDowngradingCodecSelector newInstance() {
        InternalMediaCodecUtil internalMediaCodecUtil = new InternalMediaCodecUtil();
        return new SecurityDowngradingCodecSelector(internalMediaCodecUtil);
    }

    SecurityDowngradingCodecSelector(InternalMediaCodecUtil internalMediaCodecUtil) {
        this.internalMediaCodecUtil = internalMediaCodecUtil;
    }

    @Override
    public List<MediaCodecInfo> getDecoderInfos(
            String mimeType,
            boolean requiresSecureDecoder,
            boolean requiresTunnelingDecoder
    ) throws MediaCodecUtil.DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = new ArrayList<>(internalMediaCodecUtil.getDecoderInfos(
                mimeType,
                DECODER_REQUIRES_SECURE_DECRYPTION,
                requiresTunnelingDecoder
        ));

        decoderInfos.addAll(
                internalMediaCodecUtil.getDecoderInfos(
                        mimeType,
                        DECODER_DOES_NOT_REQUIRE_SECURE_DECRYPTION,
                        requiresTunnelingDecoder
                )
        );

        return decoderInfos;
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
