package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;

import org.junit.Test;

import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_AVAILABLE;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_SKIPPED;
import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;

public class SkippedAdvertsTest {

    private static final int BEGINNING = 0;
    private static final int TEN_SECONDS_IN_MILLIS = 10000;
    private static final int TWENTY_SECONDS_IN_MILLIS = 20000;
    private static final int THIRTY_SECONDS_IN_MILLIS = 30000;
    private static final int THIRTY_FIVE_SECONDS_IN_MILLIS = 35000;
    private static final int FORTY_SECONDS_IN_MILLIS = 40000;

    private static final AdvertBreak FIRST_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TEN_SECONDS_IN_MILLIS)
            .withAdvert(anAdvert().build())
            .build();
    private static final AdvertBreak SECOND_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TWENTY_SECONDS_IN_MILLIS)
            .withAdvert(anAdvert().build())
            .build();
    private static final AdvertBreak THIRD_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(THIRTY_SECONDS_IN_MILLIS)
            .withAdvert(anAdvert().build())
            .build();
    private static final AdvertBreak FOURTH_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(FORTY_SECONDS_IN_MILLIS)
            .withAdvert(anAdvert().build())
            .build();

    private final AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK, FOURTH_ADVERT_BREAK));

    @Test
    public void doesNotSkipAdvert_whenCurrentPositionIsAtAdvertPosition() {
        AdPlaybackState adPlaybackState = SkippedAdverts.from(THIRTY_SECONDS_IN_MILLIS, advertPlaybackState.advertBreaks(), advertPlaybackState.adPlaybackState());

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void skipsAdvertsPriorToCurrentPosition() {
        AdPlaybackState adPlaybackState = SkippedAdverts.from(THIRTY_FIVE_SECONDS_IN_MILLIS, advertPlaybackState.advertBreaks(), advertPlaybackState.adPlaybackState());

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void skipsNoAdverts_whenPositionIsStart() {
        AdPlaybackState adPlaybackState = SkippedAdverts.from(BEGINNING, advertPlaybackState.advertBreaks(), advertPlaybackState.adPlaybackState());

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }
}
