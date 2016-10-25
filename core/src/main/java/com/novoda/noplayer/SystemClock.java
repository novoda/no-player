package com.novoda.noplayer;

public class SystemClock implements Clock {

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
