package com.novoda.noplayer;

import android.net.Uri;

public class Advert {

    private final long durationInMillis;
    private final Uri uri;

    public Advert(long durationInMillis, Uri uri) {
        this.durationInMillis = durationInMillis;
        this.uri = uri;
    }

    public long durationInMillis() {
        return durationInMillis;
    }

    public Uri uri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Advert advert = (Advert) o;

        if (durationInMillis != advert.durationInMillis) {
            return false;
        }
        return uri != null ? uri.equals(advert.uri) : advert.uri == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (durationInMillis ^ (durationInMillis >>> 32));
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Advert{"
                + "durationInMillis=" + durationInMillis
                + ", uri=" + uri
                + '}';
    }
}
