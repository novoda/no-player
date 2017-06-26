package com.novoda.noplayer.exoplayer;

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

public class CodecSelectorTest {

    private static final boolean ANY_USE_SECURE_DECODER = false;
    private static final String ANY_MIME_TYPE = "mimeType";

    private static final boolean OVERRIDE_WITH_SECURE_DECODER = true;
    private static final boolean OVERRIDE_WITH_INSECURE_DECODER = false;
    private static final boolean CONTENT_SECURE = true;
    private static final boolean CONTENT_INSECURE = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private CodecSelector.InternalMediaCodecUtil internalMediaCodecUtil;

    @Test
    public void givenContentIsSecure_whenOverridingWithSecure_thenRequiresSecureDecoderIsTrue() throws MediaCodecUtil.DecoderQueryException {
        CodecSelector codecSelector = new CodecSelector(internalMediaCodecUtil, OVERRIDE_WITH_SECURE_DECODER);

        codecSelector.getDecoderInfo(ANY_MIME_TYPE, CONTENT_SECURE);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfo(eq(ANY_MIME_TYPE), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isTrue();
    }

    @Test
    public void givenContentIsInsecure_whenOverridingWithInsecure_thenRequiresSecureDecoderIsFalse() throws MediaCodecUtil.DecoderQueryException {
        CodecSelector codecSelector = new CodecSelector(internalMediaCodecUtil, OVERRIDE_WITH_INSECURE_DECODER);

        codecSelector.getDecoderInfo(ANY_MIME_TYPE, CONTENT_INSECURE);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfo(eq(ANY_MIME_TYPE), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isFalse();
    }

    @Test
    public void givenContentIsSecure_whenOverridingWithInsecure_thenRequiresSecureDecoderIsFalse() throws MediaCodecUtil.DecoderQueryException {
        CodecSelector codecSelector = new CodecSelector(internalMediaCodecUtil, OVERRIDE_WITH_INSECURE_DECODER);

        codecSelector.getDecoderInfo(ANY_MIME_TYPE, CONTENT_SECURE);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfo(eq(ANY_MIME_TYPE), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isFalse();
    }

    @Test
    public void givenContentIsInsecure_whenOverridingWithSecure_thenRequiresSecureDecoderIsFalse() throws MediaCodecUtil.DecoderQueryException {
        CodecSelector codecSelector = new CodecSelector(internalMediaCodecUtil, OVERRIDE_WITH_SECURE_DECODER);

        codecSelector.getDecoderInfo(ANY_MIME_TYPE, CONTENT_INSECURE);

        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(internalMediaCodecUtil).getDecoderInfo(eq(ANY_MIME_TYPE), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isFalse();
    }

    @Test
    public void whenGettingPassthroughDecoderInfo_thenDelegates() throws MediaCodecUtil.DecoderQueryException {
        CodecSelector codecSelector = new CodecSelector(internalMediaCodecUtil, ANY_USE_SECURE_DECODER);

        codecSelector.getPassthroughDecoderInfo();

        verify(internalMediaCodecUtil).getPassthroughDecoderInfo();
    }
}
