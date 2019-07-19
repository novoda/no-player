package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class InternalMediaCodecUtilTest {

    private final MediaCodecInfo firstMediaCodec = MediaCodecInfo.newInstance("first.codec.name", "video/mp4", null);
    private final MediaCodecInfo secondMediaCodec = MediaCodecInfo.newInstance("second.codec.name", "video/mp4", null);
    private final MediaCodecInfo thirdMediaCodec = MediaCodecInfo.newInstance("third.codec.name", "video/mp4", null);

    @Test
    public void removesUnsupportedCodecs() {
        List<MediaCodecInfo> deviceMediaCodecs = Arrays.asList(firstMediaCodec, secondMediaCodec, thirdMediaCodec);
        List<String> unsupportedMediaCodec = Arrays.asList("first.codec.name", "third.codec.name");

        List<MediaCodecInfo> mediaCodecInfos = InternalMediaCodecUtil.removeUnsupportedVideoDecoders(deviceMediaCodecs, unsupportedMediaCodec);

        assertThat(mediaCodecInfos).containsExactly(secondMediaCodec);
    }
}
