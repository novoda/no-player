package com.novoda.noplayer;

import android.net.Uri;

public class Advert {

    private final AdvertId advertId;
    private final long durationInMillis;
    private final Uri uri;

    public Advert(AdvertId advertId, long durationInMillis, Uri uri) {
        this.advertId = advertId;
        this.durationInMillis = durationInMillis;
        this.uri = uri;
    }

    public AdvertId advertId() {
        return advertId;
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
        if (advertId != null ? !advertId.equals(advert.advertId) : advert.advertId != null) {
            return false;
        }
        return uri != null ? uri.equals(advert.uri) : advert.uri == null;
    }

    @Override
    public int hashCode() {
        int result = advertId != null ? advertId.hashCode() : 0;
        result = 31 * result + (int) (durationInMillis ^ (durationInMillis >>> 32));
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Advert{"
                + "advertId=" + advertId
                + ", durationInMillis=" + durationInMillis
                + ", uri=" + uri
                + '}';
    }
}
