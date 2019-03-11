package com.novoda.noplayer;

import java.util.List;

public class AdvertBreak {

    private final long startTimeInMicros;
    private final List<Advert> adverts;

    public AdvertBreak(long startTimeInMicros, List<Advert> adverts) {
        this.startTimeInMicros = startTimeInMicros;
        this.adverts = adverts;
    }

    public long startTime() {
        return startTimeInMicros;
    }

    public List<Advert> adverts() {
        return adverts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdvertBreak that = (AdvertBreak) o;

        if (startTimeInMicros != that.startTimeInMicros) {
            return false;
        }
        return adverts != null ? adverts.equals(that.adverts) : that.adverts == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (startTimeInMicros ^ (startTimeInMicros >>> 32));
        result = 31 * result + (adverts != null ? adverts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvertBreak{"
                + "startTimeInMicros=" + startTimeInMicros
                + ", adverts=" + adverts
                + '}';
    }
}
