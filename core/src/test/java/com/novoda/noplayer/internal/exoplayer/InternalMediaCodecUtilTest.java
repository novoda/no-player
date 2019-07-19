package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class InternalMediaCodecUtilTest {

    private static final int HD_QUALITY_THRESHOLD = 1000;

    private static final Format HD_FORMAT = Format.createVideoSampleFormat(null, null, null, 1000, 0, 0, 0, 0, null, null);
    private static final Format STANDARD_FORMAT = Format.createVideoSampleFormat(null, null, null, 999, 0, 0, 0, 0, null, null);
    private final MediaCodecInfo firstMediaCodec = MediaCodecInfo.newInstance("first.codec.name.secure", "video/mp4", null, false, true);
    private final MediaCodecInfo secondMediaCodec = MediaCodecInfo.newInstance("second.codec.name", "video/mp4", null);
    private final MediaCodecInfo thirdMediaCodec = MediaCodecInfo.newInstance("third.codec.name", "video/mp4", null);

    @Test
    public void removesUnsupportedCodecs() {
        List<MediaCodecInfo> deviceMediaCodecs = Arrays.asList(firstMediaCodec, secondMediaCodec, thirdMediaCodec);
        List<String> unsupportedMediaCodec = Arrays.asList("first.codec.name.secure", "third.codec.name");

        List<MediaCodecInfo> mediaCodecInfos = InternalMediaCodecUtil.removeUnsupportedVideoDecoders(deviceMediaCodecs, unsupportedMediaCodec);

        assertThat(mediaCodecInfos).containsExactly(secondMediaCodec);
    }

    @Test
    public void returnsOriginalDecoderInfos_whenFormatIsNotHighDefinition() {
        List<MediaCodecInfo> deviceMediaCodecs = Arrays.asList(firstMediaCodec, secondMediaCodec, thirdMediaCodec);

        List<MediaCodecInfo> mediaCodecInfos = InternalMediaCodecUtil.removeAllUnsecureDecodersFromHdTrack(STANDARD_FORMAT, deviceMediaCodecs, HD_QUALITY_THRESHOLD);

        assertThat(mediaCodecInfos).containsExactly(firstMediaCodec, secondMediaCodec, thirdMediaCodec);
    }

    @Test
    public void removesAllUnsecureDecoders_whenFormatIsHighDefinition() {
        List<MediaCodecInfo> deviceMediaCodecs = Arrays.asList(firstMediaCodec, secondMediaCodec, thirdMediaCodec);

        List<MediaCodecInfo> mediaCodecInfos = InternalMediaCodecUtil.removeAllUnsecureDecodersFromHdTrack(HD_FORMAT, deviceMediaCodecs, HD_QUALITY_THRESHOLD);

        assertThat(mediaCodecInfos).containsExactly(firstMediaCodec);
    }
}
