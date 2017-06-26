package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

/**
 * CodecSelector that allows overriding of the requiresSecureDecoder to allow
 * downgrading of security but not upgrading.
 */
class CodecSelector implements MediaCodecSelector {

    private final InternalMediaCodecUtil internalMediaCodecUtil;
    private final boolean overrideSecureDecoder;

    public static CodecSelector newInstance(boolean useSecureCodec) {
        InternalMediaCodecUtil internalMediaCodecUtil = new InternalMediaCodecUtil();
        return new CodecSelector(internalMediaCodecUtil, useSecureCodec);
    }

    CodecSelector(InternalMediaCodecUtil internalMediaCodecUtil, boolean overrideSecureDecoder) {
        this.internalMediaCodecUtil = internalMediaCodecUtil;
        this.overrideSecureDecoder = overrideSecureDecoder;
    }

    @Override
    public MediaCodecInfo getDecoderInfo(String mimeType, boolean contentRequiresSecureDecoder)
            throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getDecoderInfo(mimeType, contentRequiresSecureDecoder && overrideSecureDecoder);
    }

    @Override
    public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getPassthroughDecoderInfo();
    }

    static class InternalMediaCodecUtil {

        MediaCodecInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(mimeType, requiresSecureDecoder);
        }

        MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }
}
