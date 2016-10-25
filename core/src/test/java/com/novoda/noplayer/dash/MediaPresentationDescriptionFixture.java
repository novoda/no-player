package com.novoda.noplayer.dash;

import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;

import java.util.ArrayList;
import java.util.List;

public class MediaPresentationDescriptionFixture {

    private static final int MANIFEST_AVAILABILITY_START_TIME = 0;
    private static final int MANIFEST_DURATION = 0;
    private static final int MANIFEST_MIN_BUFFER_TIME = 0;
    private static final boolean MANIFEST_DYNAMIC = false;
    private static final int MANIFEST_MIN_UPDATE_PERIOD = 0;
    private static final int MANIFEST_TIME_SHIFT_BUFFER_DEPTH = 0;
    private static final UtcTimingElement MANIFEST_UTC_TIMING = new UtcTimingElement("", "");
    private static final String MANIFEST_LOCATION = "";

    private final List<Period> periods = new ArrayList<>();

    private MediaPresentationDescriptionFixture() {
        // uses static factory
    }

    public static MediaPresentationDescriptionFixture aMediaPresentationDescription() {
        return new MediaPresentationDescriptionFixture();
    }

    public MediaPresentationDescriptionFixture withPeriod(Period period) {
        periods.add(period);
        return this;
    }

    public MediaPresentationDescription build() {
        return new MediaPresentationDescription(
                MANIFEST_AVAILABILITY_START_TIME,
                MANIFEST_DURATION,
                MANIFEST_MIN_BUFFER_TIME,
                MANIFEST_DYNAMIC,
                MANIFEST_MIN_UPDATE_PERIOD,
                MANIFEST_TIME_SHIFT_BUFFER_DEPTH,
                MANIFEST_UTC_TIMING,
                MANIFEST_LOCATION,
                periods
        );
    }
}
