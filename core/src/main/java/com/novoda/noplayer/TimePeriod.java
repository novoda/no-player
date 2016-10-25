package com.novoda.noplayer;

public class TimePeriod {

    private final Time start;
    private final Time end;

    public TimePeriod(Time start, Time end) {
        this.start = start;
        this.end = end;
    }

    public Time end() {
        return end;
    }

    public boolean contains(Time currentPosition) {
        return currentPosition.isAfterOrSame(start) && currentPosition.isBeforeOrSame(end);
    }
}
