package com.novoda.noplayer;

public class AdvertBreakId {

    private final String id;

    public AdvertBreakId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdvertBreakId that = (AdvertBreakId) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AdvertBreakId{"
                + "id='" + id + '\''
                + '}';
    }
}
