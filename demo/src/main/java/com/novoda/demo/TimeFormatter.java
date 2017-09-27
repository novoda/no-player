package com.novoda.demo;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

class TimeFormatter {

    static String asHoursMinutesSeconds(int timeInSeconds) {
        int hours = (int) TimeUnit.SECONDS.toHours(timeInSeconds);
        int minutes = (int) TimeUnit.SECONDS.toMinutes(timeInSeconds - TimeUnit.HOURS.toSeconds(hours));
        int seconds = (int) (timeInSeconds - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }
}
