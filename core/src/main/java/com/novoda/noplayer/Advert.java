package com.novoda.noplayer;

import android.net.Uri;

public class Advert {

    private final long durationInMicros;
    private final Uri uri;

    public Advert(long durationInMicros, Uri uri) {
        this.durationInMicros = durationInMicros;
        this.uri = uri;
    }

    public long durationInMicros() {
        return durationInMicros;
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

        if (durationInMicros != advert.durationInMicros) {
            return false;
        }
        return uri != null ? uri.equals(advert.uri) : advert.uri == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (durationInMicros ^ (durationInMicros >>> 32));
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Advert{"
                + "durationInMicros=" + durationInMicros
                + ", uri=" + uri
                + '}';
    }
}
