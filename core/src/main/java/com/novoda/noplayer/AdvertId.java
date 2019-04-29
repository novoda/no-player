package com.novoda.noplayer;

public class AdvertId {

    private final String id;

    public AdvertId(String id) {
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

        AdvertId advertId = (AdvertId) o;

        return id != null ? id.equals(advertId.id) : advertId.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AdvertId{"
                + "id='" + id + '\''
                + '}';
    }
}
