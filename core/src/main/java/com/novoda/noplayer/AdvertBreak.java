package com.novoda.noplayer;

import java.util.List;
import java.util.concurrent.TimeUnit;

class AdvertBreak {

    private final TimeUnit startTime;
    private final List<Advert> adverts;

    public AdvertBreak(TimeUnit startTime, List<Advert> adverts) {
        this.startTime = startTime;
        this.adverts = adverts;
    }

    public TimeUnit startTime() {
        return startTime;
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

        if (startTime != that.startTime) {
            return false;
        }
        return adverts != null ? adverts.equals(that.adverts) : that.adverts == null;
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (adverts != null ? adverts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvertBreak{"
                + "startTime=" + startTime
                + ", adverts=" + adverts
                + '}';
    }
}
