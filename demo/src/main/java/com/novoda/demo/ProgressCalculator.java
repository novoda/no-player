package com.novoda.demo;

import com.novoda.noplayer.model.VideoDuration;

final class ProgressCalculator {

    private static final int MAX_PROGRESS_INCREMENTS = 100;
    private static final int MAX_PROGRESS_PERCENT = 100;

    private ProgressCalculator() {
        // Uses static methods.
    }

    static int progressAsIncrements(long positionInMillis, VideoDuration duration) {
        double percentageOfDuration = positionInMillis / (float) duration.inMillis();
        return (int) (MAX_PROGRESS_INCREMENTS * percentageOfDuration);
    }

    static int bufferAsIncrements(int bufferPercentage) {
        return (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;
    }

    static long seekToPosition(VideoDuration mediaDuration, int progress, int max) {
        float progressMultiplier = (float) progress / max;
        return mediaDuration.positionAtPercentage(progressMultiplier);
    }
}
