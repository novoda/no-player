package com.novoda.demo;

final class ProgressCalculator {

    private static final int MAX_PROGRESS_INCREMENTS = 100;
    private static final int MAX_PROGRESS_PERCENT = 100;

    private ProgressCalculator() {
        // Uses static methods.
    }

    static int progressAsIncrements(long positionInMillis, long durationInMillis) {
        double percentageOfDuration = positionInMillis / (float) durationInMillis;
        return (int) (MAX_PROGRESS_INCREMENTS * percentageOfDuration);
    }

    static int bufferAsIncrements(int bufferPercentage) {
        return (bufferPercentage * MAX_PROGRESS_INCREMENTS) / MAX_PROGRESS_PERCENT;
    }

    static long seekToPosition(long durationInMillis, int progress, int max) {
        float progressMultiplier = (float) progress / max;
        return (long) (durationInMillis * progressMultiplier);
    }
}
