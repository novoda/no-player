package com.novoda.noplayer.model;

public final class Bitrate {

    private static final int KILOBIT = 1000;

    private final long bitsPerSecond;

    public static Bitrate fromBitsPerSecond(long bitsPerSecond) {
        return new Bitrate(bitsPerSecond);
    }

    private Bitrate(long bitsPerSecond) {
        this.bitsPerSecond = bitsPerSecond;
    }

    public long asKilobits() {
        return bitsPerSecond / KILOBIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bitrate bitrate = (Bitrate) o;

        return bitsPerSecond == bitrate.bitsPerSecond;
    }

    @Override
    public int hashCode() {
        return (int) (bitsPerSecond ^ (bitsPerSecond >>> 32));
    }
}
