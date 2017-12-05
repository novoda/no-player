package com.novoda.demo;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TimeFormatterTest {

    @Test
    public void givenOverOneHourInMillis_whenFormatting_thenReturnsAsHoursMinutesSeconds() {
        long oneHourTwelveMinutesAndFifteenSecondsAsMillis = 4335000;

        String asHoursMinutesSeconds = TimeFormatter.asHoursMinutesSeconds(oneHourTwelveMinutesAndFifteenSecondsAsMillis);

        assertThat(asHoursMinutesSeconds).isEqualTo("1:12:15");
    }

    @Test
    public void givenLessThanOneHourInMillis_whenFormatting_thenReturnsAsMinutesAndSeconds() {
        long fiftyOneMinutesAndThirtyFiveSecondsAsMillis = 3095000;

        String asHoursMinutesSeconds = TimeFormatter.asHoursMinutesSeconds(fiftyOneMinutesAndThirtyFiveSecondsAsMillis);

        assertThat(asHoursMinutesSeconds).isEqualTo("51:35");
    }
}
