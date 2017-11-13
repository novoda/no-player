package com.novoda.noplayer.model;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public final class VideoDuration implements Serializable {

    public static final VideoDuration ZERO = new VideoDuration(0);
    public static final VideoDuration INVALID = new VideoDuration(-1);

    private static final long SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final long durationInMillis;

    public static VideoDuration fromMillis(long durationInMillis) {
        return new VideoDuration(durationInMillis);
    }

    public static VideoDuration fromSeconds(double durationInSeconds) {
        return new VideoDuration((long) (durationInSeconds * SECOND_IN_MILLIS));
    }

    private VideoDuration(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public long positionAtPercentage(float percentage) {
        return (long) (durationInMillis * percentage);
    }

    public long inMillis() {
        return durationInMillis;
    }

    public int inImpreciseMillis() {
        return (int) durationInMillis;
    }

    public double inSeconds() {
        return durationInMillis / (double) SECOND_IN_MILLIS;
    }

    public int inImpreciseSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
    }

    public int inImpreciseMinutes() {
        return (int) TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
    }

    public VideoDuration plus(VideoDuration other) {
        return VideoDuration.fromMillis(durationInMillis + other.durationInMillis);
    }

    public VideoDuration minus(VideoDuration other) {
        return VideoDuration.fromMillis(durationInMillis - other.durationInMillis);
    }

    public boolean isValid() {
        return durationInMillis >= 0;
    }

    public boolean isInvalid() {
        return durationInMillis < 0;
    }

    public boolean isZero() {
        return ZERO.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VideoDuration that = (VideoDuration) o;

        return durationInMillis == that.durationInMillis;
    }

    @Override
    public int hashCode() {
        return (int) (durationInMillis ^ (durationInMillis >>> 32));
    }

    @Override
    public String toString() {
        return "VideoDuration{"
                + "durationInMillis=" + durationInMillis
                + '}';
    }
}
