package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class SecurityDowngradingCodecSelectorTest {

    private static final String ANY_MIME_TYPE = "mimeType";

    private static final boolean CONTENT_SECURE = true;
    private static final boolean CONTENT_INSECURE = false;
    private static final boolean DOES_NOT_REQUIRES_TUNNELING_DECODER = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SecurityDowngradingCodecSelector.InternalMediaCodecUtil internalMediaCodecUtil;

    @Test
    public void whenContentIsSecure_thenRequiresSecureDecoderIsFalse() throws MediaCodecUtil.DecoderQueryException {
        SecurityDowngradingCodecSelector securityDowngradingCodecSelector = new SecurityDowngradingCodecSelector(internalMediaCodecUtil);

        securityDowngradingCodecSelector.getDecoderInfos(ANY_MIME_TYPE, CONTENT_SECURE, DOES_NOT_REQUIRE_TUNNELING_DECODER);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfos(eq(ANY_MIME_TYPE), argumentCaptor.capture(), eq(DOES_NOT_REQUIRES_TUNNELING_DECODER));
        assertThat(argumentCaptor.getValue()).isFalse();
    }

    @Test
    public void whenContentIsInsecure_thenRequiresSecureDecoderIsFalse() throws MediaCodecUtil.DecoderQueryException {
        SecurityDowngradingCodecSelector securityDowngradingCodecSelector = new SecurityDowngradingCodecSelector(internalMediaCodecUtil);

        securityDowngradingCodecSelector.getDecoderInfos(ANY_MIME_TYPE, CONTENT_INSECURE, DOES_NOT_REQUIRES_TUNNELING_DECODER);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfos(eq(ANY_MIME_TYPE), argumentCaptor.capture(), eq(DOES_NOT_REQUIRES_TUNNELING_DECODER));
        assertThat(argumentCaptor.getValue()).isFalse();
    }

    @Test
    public void whenGettingPassthroughDecoderInfo_thenDelegates() throws MediaCodecUtil.DecoderQueryException {
        SecurityDowngradingCodecSelector securityDowngradingCodecSelector = new SecurityDowngradingCodecSelector(internalMediaCodecUtil);

        securityDowngradingCodecSelector.getPassthroughDecoderInfo();

        verify(internalMediaCodecUtil).getPassthroughDecoderInfo();
    }
}
