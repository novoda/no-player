package com.novoda.noplayer;

import java.util.ArrayList;
import java.util.List;

public class AdvertBreakFixtures {

    private AdvertBreakId advertBreakId = new AdvertBreakId("advert_break_id");
    private long startTimeInMillis = 1000;
    private List<Advert> adverts = new ArrayList<>();

    public static AdvertBreakFixtures anAdvertBreak() {
        return new AdvertBreakFixtures();
    }

    public AdvertBreakFixtures withAdvertBreakId(String advertBreakId) {
        return withAdvertBreakId(new AdvertBreakId(advertBreakId));
    }

    public AdvertBreakFixtures withAdvertBreakId(AdvertBreakId advertBreakId) {
        this.advertBreakId = advertBreakId;
        return this;
    }

    public AdvertBreakFixtures withStartTimeInMillis(long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
        return this;
    }

    public AdvertBreakFixtures withAdvert(Advert advert) {
        adverts.add(advert);
        return this;
    }

    public AdvertBreak build() {
        return new AdvertBreak(advertBreakId, startTimeInMillis, adverts);
    }
}
