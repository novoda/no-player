package com.novoda.noplayer.internal.exoplayer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class FramesPerSecondCalculator {

    private static final int NUMBER_OF_FRAMES_TO_CAPTURE = 100;
    private static final double ONE_SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final Deque<Long> frameTimes = new LinkedList<>();

    public FramesPerSecondCalculator() {
        frameTimes.add(System.currentTimeMillis());
    }

    public double calculate() {
        long currrentTimeInMillis = System.currentTimeMillis();

        frameTimes.addLast(currrentTimeInMillis);
        int size = frameTimes.size();
        if (size > NUMBER_OF_FRAMES_TO_CAPTURE) {
            frameTimes.removeFirst();
        }

        if (size < NUMBER_OF_FRAMES_TO_CAPTURE) {
            return -1;
        }

        Long startTimeInMillis = frameTimes.getFirst();
        double numberOfSecondsElapsed = (currrentTimeInMillis - startTimeInMillis) / ONE_SECOND_IN_MILLIS;
        return frameTimes.size() / numberOfSecondsElapsed;
    }

}
