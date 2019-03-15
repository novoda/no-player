package com.novoda.noplayer;

import java.util.List;

public class AdvertBreak {

    private final long startTimeInMillis;
    private final List<Advert> adverts;

    public AdvertBreak(long startTimeInMillis, List<Advert> adverts) {
        this.startTimeInMillis = startTimeInMillis;
        this.adverts = adverts;
    }

    public long startTimeInMillis() {
        return startTimeInMillis;
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

        if (startTimeInMillis != that.startTimeInMillis) {
            return false;
        }
        return adverts != null ? adverts.equals(that.adverts) : that.adverts == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (startTimeInMillis ^ (startTimeInMillis >>> 32));
        result = 31 * result + (adverts != null ? adverts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvertBreak{"
                + "startTimeInMillis=" + startTimeInMillis
                + ", adverts=" + adverts
                + '}';
    }
}
