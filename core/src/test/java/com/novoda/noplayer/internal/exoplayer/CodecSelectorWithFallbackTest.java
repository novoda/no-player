package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class CodecSelectorWithFallbackTest {

    private static final boolean CONTENT_SECURE = true;
    private static final boolean CONTENT_INSECURE = false;
    private static final boolean DOES_NOT_REQUIRE_TUNNELING_DECODER = false;

    private static final String MIMETYPE = "mimetype";
    private static final MediaCodecInfo SECURE_CODEC = MediaCodecInfo.newInstance("secure-codec", MIMETYPE, null);
    private static final MediaCodecInfo UNSECURE_CODEC = MediaCodecInfo.newInstance("unsecure-codec", "mimetype", null);

    private static final List<MediaCodecInfo> SECURE_CODECS = Collections.singletonList(SECURE_CODEC);
    private static final List<MediaCodecInfo> UNSECURE_CODECS = Collections.singletonList(UNSECURE_CODEC);
    private static final List<MediaCodecInfo> BOTH_CODECS = Arrays.asList(SECURE_CODEC, UNSECURE_CODEC);
    private static final List<MediaCodecInfo> NO_CODECS = Collections.emptyList();

    private final InternalMediaCodecUtil internalMediaCodecUtil = mock(InternalMediaCodecUtil.class);

    @Parameterized.Parameter
    public boolean secureCodecRequest;
    @Parameterized.Parameter(1)
    public List<MediaCodecInfo> secureDecoders;
    @Parameterized.Parameter(2)
    public List<MediaCodecInfo> unsecureDecoders;
    @Parameterized.Parameter(3)
    public List<MediaCodecInfo> decodersReturned;

    @Parameterized.Parameters(name = "given request secure codecs {0}, when device contains secure decoders {1} and unsecure decoders {2} then returns {3}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{CONTENT_SECURE, NO_CODECS, NO_CODECS, NO_CODECS},
                new Object[]{CONTENT_SECURE, NO_CODECS, UNSECURE_CODECS, UNSECURE_CODECS},
                new Object[]{CONTENT_SECURE, SECURE_CODECS, NO_CODECS, SECURE_CODECS},
                new Object[]{CONTENT_SECURE, SECURE_CODECS, UNSECURE_CODECS, BOTH_CODECS},

                new Object[]{CONTENT_INSECURE, NO_CODECS, NO_CODECS, NO_CODECS},
                new Object[]{CONTENT_INSECURE, NO_CODECS, UNSECURE_CODECS, UNSECURE_CODECS},
                new Object[]{CONTENT_INSECURE, SECURE_CODECS, NO_CODECS, NO_CODECS},
                new Object[]{CONTENT_INSECURE, SECURE_CODECS, UNSECURE_CODECS, UNSECURE_CODECS}
        );
    }

    @Test
    public void whenRequestingSecureDecoders_thenReturnsTheCorrectInternalDecodersList() throws MediaCodecUtil.DecoderQueryException {
        given(internalMediaCodecUtil.getDecoderInfos(MIMETYPE, CONTENT_SECURE, DOES_NOT_REQUIRE_TUNNELING_DECODER)).willReturn(secureDecoders);
        given(internalMediaCodecUtil.getDecoderInfos(MIMETYPE, CONTENT_INSECURE, DOES_NOT_REQUIRE_TUNNELING_DECODER)).willReturn(unsecureDecoders);

        CodecSelectorWithFallback codecSelectorWithFallback = new CodecSelectorWithFallback(internalMediaCodecUtil);
        List<MediaCodecInfo> decoderInfos = codecSelectorWithFallback.getDecoderInfos(MIMETYPE, secureCodecRequest, DOES_NOT_REQUIRE_TUNNELING_DECODER);

        assertThat(decoderInfos).isEqualTo(decodersReturned);
    }
}
