package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_AVAILABLE;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_PLAYED;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_SKIPPED;
import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;

public class AvailableAdvertsTest {

    private static final int BEGINNING = 0;
    private static final int TEN_SECONDS_IN_MILLIS = 10000;
    private static final int TWENTY_SECONDS_IN_MILLIS = 20000;
    private static final int THIRTY_SECONDS_IN_MILLIS = 30000;
    private static final int FORTY_SECONDS_IN_MILLIS = 40000;
    private static final int END = 40001;

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
    private static final List<AdvertBreak> ADVERT_BREAKS = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK, FOURTH_ADVERT_BREAK);

    @Test
    public void makesSkippedAdvertsAfterCurrentOrAtCurrentPositionAvailable() {
        AdPlaybackState adPlaybackState = adPlaybackStateWithSkippedAdverts();
        AdPlaybackState modifiedAdPlaybackState = AvailableAdverts.fromSkipped(THIRTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, adPlaybackState);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void marksAllAsAvailableWhenPositionIsBeginning() {
        AdPlaybackState adPlaybackState = adPlaybackStateWithSkippedAdverts();
        AdPlaybackState modifiedAdPlaybackState = AvailableAdverts.fromSkipped(BEGINNING, ADVERT_BREAKS, adPlaybackState);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void leavesAsCurrentStateWhenNotPreviouslySkipped() {
        AdPlaybackState adPlaybackState = adPlaybackStateWithPlayedAdverts();
        AdPlaybackState modifiedAdPlaybackState = AvailableAdverts.fromSkipped(BEGINNING, ADVERT_BREAKS, adPlaybackState);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_PLAYED});
    }

    @Test
    public void leavesAsSkippedWhenPositionIsAfterAllAdvertStartPositions() {
        AdPlaybackState adPlaybackState = adPlaybackStateWithSkippedAdverts();
        AdPlaybackState modifiedAdPlaybackState = AvailableAdverts.fromSkipped(END, ADVERT_BREAKS, adPlaybackState);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    private AdPlaybackState adPlaybackStateWithSkippedAdverts() {
        AdPlaybackState adPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        adPlaybackState = adPlaybackState.withSkippedAdGroup(0);
        adPlaybackState = adPlaybackState.withSkippedAdGroup(1);
        adPlaybackState = adPlaybackState.withSkippedAdGroup(2);
        adPlaybackState = adPlaybackState.withSkippedAdGroup(3);
        return adPlaybackState;
    }

    private AdPlaybackState adPlaybackStateWithPlayedAdverts() {
        AdPlaybackState adPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        adPlaybackState = adPlaybackState.withPlayedAd(0, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(1, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(2, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(3, 0);
        return adPlaybackState;
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }

}
