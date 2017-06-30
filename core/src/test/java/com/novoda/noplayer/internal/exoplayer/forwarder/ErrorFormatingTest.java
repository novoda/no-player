package com.novoda.noplayer.internal.exoplayer.forwarder;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ErrorFormatingTest {

    private static final String MESSAGE = "message";

    @Test
    public void givenThrowable_whenFormattingMessage_thenReturnsExpectedMessageFormat() {
        String expectedFormat = "com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatingTest$IncorrectFormatThrowable: message";

        String actualFormat = ErrorFormating.formatMessage(new IncorrectFormatThrowable(MESSAGE));

        assertThat(actualFormat).isEqualTo(expectedFormat);
    }

    private class IncorrectFormatThrowable extends Throwable {

        IncorrectFormatThrowable(String message) {
            super(message);
        }
    }
}
