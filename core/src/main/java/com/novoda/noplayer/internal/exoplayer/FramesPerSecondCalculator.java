package com.novoda.noplayer.internal.exoplayer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

class FramesPerSecondCalculator {

    private static final int NUMBER_OF_FRAMES_TO_CAPTURE = 100;
    private static final double ONE_SECOND_IN_MICRO_SECONDS = TimeUnit.SECONDS.toMicros(1);
    private final Deque<Long> frameTimes = new LinkedList<>();

    int calculate(long currentTimeInMicroSeconds) {
        frameTimes.addLast(currentTimeInMicroSeconds);
        int size = frameTimes.size();
        if (size > NUMBER_OF_FRAMES_TO_CAPTURE) {
            frameTimes.removeFirst();
        }

        Long startTimeInMicroSeconds = frameTimes.getFirst();
        double numberOfSecondsElapsed = (currentTimeInMicroSeconds - startTimeInMicroSeconds) / ONE_SECOND_IN_MICRO_SECONDS;

        return (int) (frameTimes.size() / numberOfSecondsElapsed);
    }

}
