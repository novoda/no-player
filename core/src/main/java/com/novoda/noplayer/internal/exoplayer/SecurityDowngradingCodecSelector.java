package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

class SecurityDowngradingCodecSelector implements MediaCodecSelector {

    private static final boolean USE_INSECURE_DECODER = false;

    private final InternalMediaCodecUtil internalMediaCodecUtil;

    public static SecurityDowngradingCodecSelector newInstance() {
        InternalMediaCodecUtil internalMediaCodecUtil = new InternalMediaCodecUtil();
        return new SecurityDowngradingCodecSelector(internalMediaCodecUtil);
    }

    SecurityDowngradingCodecSelector(InternalMediaCodecUtil internalMediaCodecUtil) {
        this.internalMediaCodecUtil = internalMediaCodecUtil;
    }

    @Override
    public MediaCodecInfo getDecoderInfo(String mimeType, boolean contentRequiresSecureDecoder)
            throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getDecoderInfo(mimeType, USE_INSECURE_DECODER);
    }

    @Override
    public MediaCodecInfo getPassthroughDecoderInfo() {
        return internalMediaCodecUtil.getPassthroughDecoderInfo();
    }

    static class InternalMediaCodecUtil {

        MediaCodecInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(mimeType, requiresSecureDecoder);
        }

        MediaCodecInfo getPassthroughDecoderInfo() {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }
}
