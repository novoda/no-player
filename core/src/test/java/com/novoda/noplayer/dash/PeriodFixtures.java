package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.Period;

import java.util.ArrayList;
import java.util.List;

public class PeriodFixtures {

    private static final String PERIOD_ID = "";
    private static final int PERIOD_START = 0;

    private final List<AdaptationSet> adaptationSetList = new ArrayList<>();

    public static PeriodFixtures aPeriod() {
        return new PeriodFixtures();
    }

    public PeriodFixtures withAdaptationSet(AdaptationSet adaptionSet) {
        adaptationSetList.add(adaptionSet);
        return this;
    }

    public Period build() {
        return new Period(PERIOD_ID, PERIOD_START, adaptationSetList);
    }
}
