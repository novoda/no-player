package com.novoda.noplayer;

import android.net.Uri;

import static org.mockito.Mockito.mock;

public class AdvertFixtures {

    private AdvertId advertId = new AdvertId("advert_id");
    private long durationInMillis = 1000;
    private Uri uri = mock(Uri.class);

    public static AdvertFixtures anAdvert() {
        return new AdvertFixtures();
    }

    public AdvertFixtures withAdvertId(String advertId) {
        return withAdvertId(new AdvertId(advertId));
    }

    public AdvertFixtures withAdvertId(AdvertId advertId) {
        this.advertId = advertId;
        return this;
    }

    public AdvertFixtures withDurationInMillis(long durationInMillis) {
        this.durationInMillis = durationInMillis;
        return this;
    }

    public AdvertFixtures withUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public Advert build() {
        return new Advert(advertId, durationInMillis, uri);
    }
}
