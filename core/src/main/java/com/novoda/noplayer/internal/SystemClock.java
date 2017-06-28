package com.novoda.noplayer.internal;

public class SystemClock implements Clock {

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
