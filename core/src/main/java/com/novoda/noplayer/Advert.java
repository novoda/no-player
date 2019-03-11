package com.novoda.noplayer;

import android.net.Uri;

import java.util.concurrent.TimeUnit;

class Advert {

    private final TimeUnit duration;
    private final Uri uri;

    public Advert(TimeUnit duration, Uri uri) {
        this.duration = duration;
        this.uri = uri;
    }

    public TimeUnit duration() {
        return duration;
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

        if (duration != advert.duration) {
            return false;
        }
        return uri != null ? uri.equals(advert.uri) : advert.uri == null;
    }

    @Override
    public int hashCode() {
        int result = duration != null ? duration.hashCode() : 0;
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Advert{"
                + "duration=" + duration
                + ", uri=" + uri
                + '}';
    }
}
