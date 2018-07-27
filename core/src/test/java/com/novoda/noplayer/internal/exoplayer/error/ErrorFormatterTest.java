package com.novoda.noplayer.internal.exoplayer.error;


import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ErrorFormatterTest {

    private static final String MESSAGE = "message";

    @Test
    public void givenThrowable_whenFormattingMessage_thenReturnsExpectedMessageFormat() {
        String expectedFormat = "com.novoda.noplayer.internal.exoplayer.error.ErrorFormatterTest$IncorrectFormatThrowable: message";

        String actualFormat = ErrorFormatter.formatMessage(new IncorrectFormatThrowable(MESSAGE));

        assertThat(actualFormat).isEqualTo(expectedFormat);
    }

    private class IncorrectFormatThrowable extends Throwable {

        IncorrectFormatThrowable(String message) {
            super(message);
        }
    }
}
