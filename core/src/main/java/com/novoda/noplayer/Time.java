package com.novoda.noplayer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Time implements Comparable<Time>, Serializable {

    public static final Time ZERO = fromMillis(0);
    public static final Time INVALID = fromMillis(-1);
    private static final long SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final long millis;

    Time(long millis) {
        this.millis = millis;
    }

    public static Time fromMillis(long value) {
        return new Time(value);
    }

    public static Time fromSeconds(double seconds) {
        return new Time((long) (seconds * SECOND_IN_MILLIS));
    }

    public long toMillis() {
        return millis;
    }

    public int toIntMillis() {
        return (int) millis;
    }

    public float proportionOf(Time duration) {
        return millis / (float) duration.millis;
    }

    public Time multiplyBy(float multiplier) {
        return fromMillis((long) (millis * multiplier));
    }

    public Time subtract(Time position) {
        return fromMillis(millis - position.millis);
    }

    public Time add(Time position) {
        return fromMillis(millis + position.millis);
    }

    public double toSeconds() {
        return millis / (double) SECOND_IN_MILLIS;
    }

    public int toImpreciseSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public int toImpreciseMinutes() {
        return (int) TimeUnit.MILLISECONDS.toMinutes(millis);
    }

    public boolean isBefore(Time otherTime) {
        return otherTime.toMillis() > toMillis();
    }

    public boolean isBeforeOrSame(Time otherTime) {
        return otherTime.toMillis() >= toMillis();
    }

    public boolean isAfter(Time otherTime) {
        return otherTime.toMillis() < toMillis();
    }

    public boolean isAfterOrSame(Time otherTime) {
        return otherTime.toMillis() <= toMillis();
    }

    public boolean equalsImpreciseSeconds(Time otherTime) {
        return toImpreciseSeconds() == otherTime.toImpreciseSeconds();
    }

    @Override
    public String toString() {
        return "Time in millis: " + millis;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Time && ((Time) o).millis == millis;
    }

    @Override
    public int hashCode() {
        return (int) (millis ^ (millis >>> 32));
    }

    @Override
    public int compareTo(Time another) {
        if (millis > another.millis) {
            return 1;
        } else if (millis == another.millis) {
            return 0;
        } else {
            return -1;
        }
    }

    public static class Window {

        private final Time start;
        private final Time end;

        public Window(Time start, Time end) {
            this.start = start;
            this.end = end;
        }

        public Time getStart() {
            return start;
        }

        public Time getEnd() {
            return end;
        }
    }
}
