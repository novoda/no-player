package com.novoda.noplayer.internal.exoplayer.error;

import android.media.MediaCodec;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ErrorFormatterTest {

    private static final String MESSAGE = "message";

    @Test
    public void givenThrowable_whenFormattingMessage_thenReturnsExpectedMessageFormat() {
        String expectedFormat = "com.novoda.noplayer.internal.exoplayer.error.ErrorFormatterTest$IncorrectFormatThrowable: message";

        String actualFormat = ErrorFormatter.formatMessage(new IncorrectFormatThrowable(MESSAGE));

        assertThat(actualFormat).isEqualTo(expectedFormat);
    }

    @Test
    public void givenMediaCodecException_whenFormattingMessage_thenReturnsExpectedMessageFormat() {
        MediaCodec.CodecException codecException = mock(MediaCodec.CodecException.class);
        given(codecException.getDiagnosticInfo()).willReturn("android.media.MediaCodec.error_+1234");
        given(codecException.isTransient()).willReturn(true);
        given(codecException.isRecoverable()).willReturn(false);

        String expectedFormat = "diagnosticInformation=android.media.MediaCodec.error_+1234 : isTransient=true : isRecoverable=false";
        String actualFormat = ErrorFormatter.formatCodecException(codecException);

        assertThat(actualFormat).isEqualTo(expectedFormat);
    }

    private class IncorrectFormatThrowable extends Throwable {

        IncorrectFormatThrowable(String message) {
            super(message);
        }
    }
}
