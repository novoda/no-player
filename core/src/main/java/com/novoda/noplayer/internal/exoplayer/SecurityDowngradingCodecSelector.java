package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

class SecurityDowngradingCodecSelector implements MediaCodecSelector {

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
        return internalMediaCodecUtil.getDecoderInfo(mimeType, false);
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
