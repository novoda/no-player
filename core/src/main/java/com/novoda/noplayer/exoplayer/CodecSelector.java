package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

class CodecSelector implements MediaCodecSelector {

    private final boolean useSecureCodec;

    CodecSelector(boolean useSecureCodec) {
        this.useSecureCodec = useSecureCodec;
    }

    @Override
    public MediaCodecInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder)
            throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfo(mimeType, useSecureCodec);
    }

    @Override
    public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getPassthroughDecoderInfo();
    }
}
