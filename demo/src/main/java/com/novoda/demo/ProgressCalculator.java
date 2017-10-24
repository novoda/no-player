package com.novoda.demo;

import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

final class ProgressCalculator {

    private static final int MAX_PROGRESS_INCREMENTS = 100;
    private static final int MAX_PROGRESS_PERCENT = 100;

    private ProgressCalculator() {
        // Uses static methods.
    }

    static int progressAsIncrements(VideoPosition position, VideoDuration duration) {
        double percentageOfDuration = position.asPercentageOf(duration);
        return (int) (MAX_PROGRESS_INCREMENTS * percentageOfDuration);
    }

    static int bufferAsIncrements(int bufferPercentage) {
        return (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;
    }
}
