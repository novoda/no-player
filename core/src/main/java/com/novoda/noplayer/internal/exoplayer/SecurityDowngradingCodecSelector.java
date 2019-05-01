package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.List;

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
    public List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getDecoderInfos(mimeType, USE_INSECURE_DECODER);
    }

    @Override
    public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return internalMediaCodecUtil.getPassthroughDecoderInfo();
    }

    static class InternalMediaCodecUtil {

        List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder);
        }

        MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }
}
