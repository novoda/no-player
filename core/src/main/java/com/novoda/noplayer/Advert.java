package com.novoda.noplayer;

import android.net.Uri;
import android.support.annotation.Nullable;

public class Advert {

    private final AdvertId advertId;
    private final long durationInMillis;
    private final Uri uri;
    @Nullable
    private final Uri clickThroughUri;

    public Advert(AdvertId advertId, long durationInMillis, Uri uri, @Nullable Uri clickThroughUri) {
        this.advertId = advertId;
        this.durationInMillis = durationInMillis;
        this.uri = uri;
        this.clickThroughUri = clickThroughUri;
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

    @Nullable
    public Uri clickThroughUri() {
        return clickThroughUri;
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
        if (uri != null ? !uri.equals(advert.uri) : advert.uri != null) {
            return false;
        }
        return clickThroughUri != null ? clickThroughUri.equals(advert.clickThroughUri) : advert.clickThroughUri == null;
    }

    @Override
    public int hashCode() {
        int result = advertId != null ? advertId.hashCode() : 0;
        result = 31 * result + (int) (durationInMillis ^ (durationInMillis >>> 32));
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (clickThroughUri != null ? clickThroughUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Advert{"
                + "advertId=" + advertId
                + ", durationInMillis=" + durationInMillis
                + ", uri=" + uri
                + ", clickThroughUri=" + clickThroughUri
                + '}';
    }
}
