package com.novoda.demo;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TimeFormatterTest {

    @Test
    public void givenOverOneHourInSeconds_whenFormatting_thenReturnsAsHoursMinutesSeconds() {
        int oneHourTwelveMinutesAndFifteenSecondsAsSeconds = 4335;

        String asHoursMinutesSeconds = TimeFormatter.asHoursMinutesSeconds(oneHourTwelveMinutesAndFifteenSecondsAsSeconds);

        assertThat(asHoursMinutesSeconds).isEqualTo("1:12:15");
    }

    @Test
    public void givenLessThanOneHourInSeconds_whenFormatting_thenReturnsAsMinutesAndSeconds() {
        int fiftyOneMinutesAndThirtyFiveSecondsAsSeconds = 3095;

        String asHoursMinutesSeconds = TimeFormatter.asHoursMinutesSeconds(fiftyOneMinutesAndThirtyFiveSecondsAsSeconds);

        assertThat(asHoursMinutesSeconds).isEqualTo("51:35");
    }
}
