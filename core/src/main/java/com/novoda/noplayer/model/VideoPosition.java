package com.novoda.noplayer.model;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public final class VideoPosition implements Comparable<VideoPosition>, Serializable {

    public static final VideoPosition BEGINNING = new VideoPosition(0);
    public static final VideoPosition INVALID = new VideoPosition(-1);

    private static final long SECOND_IN_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private final long positionInMillis;

    public static VideoPosition fromMillis(long positionInMillis) {
        return new VideoPosition(positionInMillis);
    }

    public static VideoPosition fromSeconds(double positionInSeconds) {
        return new VideoPosition((long) (positionInSeconds * SECOND_IN_MILLIS));
    }

    private VideoPosition(long positionInMillis) {
        this.positionInMillis = positionInMillis;
    }

    public boolean isAfter(VideoPosition otherPosition) {
        return positionInMillis > otherPosition.positionInMillis;
    }

    public boolean isBefore(VideoPosition otherPosition) {
        return positionInMillis < otherPosition.positionInMillis;
    }

    public long inMillis() {
        return positionInMillis;
    }

    public int inImpreciseMillis() {
        return (int) positionInMillis;
    }

    public int inImpreciseSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(positionInMillis);
    }

    public double inSeconds() {
        return positionInMillis / (double) SECOND_IN_MILLIS;
    }

    public int inImpreciseMinutes() {
        return (int) TimeUnit.MILLISECONDS.toMinutes(positionInMillis);
    }

    public VideoPosition plus(VideoPosition other) {
        return VideoPosition.fromMillis(positionInMillis + other.positionInMillis);
    }

    public VideoPosition minus(VideoPosition other) {
        return VideoPosition.fromMillis(positionInMillis - other.positionInMillis);
    }

    public VideoPosition multiplyBy(float multiplyBy) {
        return VideoPosition.fromMillis((long) (positionInMillis * multiplyBy));
    }

    public double asPercentageOf(VideoDuration videoDuration) {
        return positionInMillis / (float) videoDuration.inMillis();
    }

    public boolean isValid() {
        return positionInMillis >= 0;
    }

    public boolean isInvalid() {
        return positionInMillis < 0;
    }

    public boolean isBeginning() {
        return BEGINNING.equals(this);
    }

    @Override
    public int compareTo(VideoPosition other) {
        if (positionInMillis > other.positionInMillis) {
            return 1;
        } else if (positionInMillis == other.positionInMillis) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VideoPosition that = (VideoPosition) o;

        return positionInMillis == that.positionInMillis;
    }

    @Override
    public int hashCode() {
        return (int) (positionInMillis ^ (positionInMillis >>> 32));
    }

    @Override
    public String toString() {
        return "VideoPosition{"
                + "positionInMillis=" + positionInMillis
                + '}';
    }
}
