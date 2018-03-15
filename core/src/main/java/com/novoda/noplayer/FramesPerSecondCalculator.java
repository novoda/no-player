package com.novoda.noplayer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class FramesPerSecondCalculator {

    private static final int NUMBER_OF_FRAMES_TO_CAPTURE = 100;
    private static final double ONE_SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final LinkedList<Long> frameTimes = new LinkedList<Long>() {{
        add(System.currentTimeMillis());
    }};

    public double calculate() {
        long currrentTimeInMillis = System.currentTimeMillis();
        Long startTimeInMillis = frameTimes.getFirst();
        double numberOfSecondsElapsed = (currrentTimeInMillis - startTimeInMillis) / ONE_SECOND_IN_MILLIS;

        frameTimes.addLast(currrentTimeInMillis);
        int size = frameTimes.size();
        if (size > NUMBER_OF_FRAMES_TO_CAPTURE) {
            frameTimes.removeFirst();
        }

        return frameTimes.size() / numberOfSecondsElapsed;
    }

}
