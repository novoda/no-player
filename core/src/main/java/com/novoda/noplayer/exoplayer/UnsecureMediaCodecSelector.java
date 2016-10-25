package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.DecoderInfo;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecUtil;

class UnsecureMediaCodecSelector implements MediaCodecSelector {

    private static final boolean UNSECURE_CODEC = false;

    @Override
    public DecoderInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfo(mimeType, UNSECURE_CODEC);
    }

    @Override
    public DecoderInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getPassthroughDecoderInfo();
    }
}
