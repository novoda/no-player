package com.novoda.noplayer.internal.mediaplayer;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ErrorFormatingTest {

    private static final int TYPE_CODE = 202;
    private static final int EXTRA_CODE = -218;

    @Test
    public void givenTypeAndExtra_whenFormattingMessage_thenReturnsExpectedMessageFormat() {
        String expectedFormat = "Type: 202, Extra: -218";

        String actualFormat = ErrorFormating.formatMessage(TYPE_CODE, EXTRA_CODE);

        assertThat(actualFormat).isEqualTo(expectedFormat);
    }
}
