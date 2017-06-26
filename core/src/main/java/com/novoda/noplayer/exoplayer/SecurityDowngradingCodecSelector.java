package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

class SecurityDowngradingCodecSelector implements MediaCodecSelector {

    private final InternalMediaCodecUtil internalMediaCodecUtil;
    private final boolean downgradeSecureDecoder;

    public static SecurityDowngradingCodecSelector newInstance(boolean downgradeSecureDecoder) {
        InternalMediaCodecUtil internalMediaCodecUtil = new InternalMediaCodecUtil();
        return new SecurityDowngradingCodecSelector(internalMediaCodecUtil, downgradeSecureDecoder);
    }

    SecurityDowngradingCodecSelector(InternalMediaCodecUtil internalMediaCodecUtil, boolean downgradeSecureDecoder) {
        this.internalMediaCodecUtil = internalMediaCodecUtil;
        this.downgradeSecureDecoder = downgradeSecureDecoder;
    }

    @Override
    public MediaCodecInfo getDecoderInfo(String mimeType, boolean contentRequiresSecureDecoder)
            throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getDecoderInfo(mimeType, contentRequiresSecureDecoder && !downgradeSecureDecoder);
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
