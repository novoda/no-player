package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_AVAILABLE;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_PLAYED;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_SKIPPED;
import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;

public class SkippedAdvertsTest {

    private static final int TEN_SECONDS_IN_MILLIS = 10000;
    private static final int TWENTY_SECONDS_IN_MILLIS = 20000;
    private static final int THIRTY_SECONDS_IN_MILLIS = 30000;
    private static final int FORTY_SECONDS_IN_MILLIS = 40000;

    private static final AdvertBreak FIRST_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TEN_SECONDS_IN_MILLIS)
            .withAdvert(anAdvert().build())
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
    public void skipsAllAdverts() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void skipAdvertBreak() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(0, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void skipAdvert() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAdvertAsSkipped(0, 0, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotSkipAdvertThatHasAlreadyPlayed() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        initialAvailableAdPlaybackState = initialAvailableAdPlaybackState.withPlayedAd(1, 0);
        AdPlaybackState adPlaybackState = SkippedAdverts.markAdvertBreakAsSkipped(ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void makesAvailableAdvertsBeforeCurrentPositionSkipped() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllPastAvailableAdvertsAsSkipped(TWENTY_SECONDS_IN_MILLIS + 1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotMarkAdvertsAlreadyPlayedAsSkipped() {
        AdPlaybackState initialAvailableAdPlaybackState = adPlaybackStateWithPlayedAdverts();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllPastAvailableAdvertsAsSkipped(TWENTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED, AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_PLAYED});
    }

    @Test
    public void markCurrentGateAsSkipped() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(TWENTY_SECONDS_IN_MILLIS + 1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void markCurrentGateAsSkippedFromTheVeryBegginingOfPeriod() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(TWENTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }


    @Test
    public void doesNotMarkCurrentGateAsSkippedIfPlayedAlready() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        initialAvailableAdPlaybackState = initialAvailableAdPlaybackState.withPlayedAd(1, 0);
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(TWENTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotMarkCurrentGateAsSkippedIfPlayedFirstAlready() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        initialAvailableAdPlaybackState = initialAvailableAdPlaybackState.withPlayedAd(0, 0);
        initialAvailableAdPlaybackState = initialAvailableAdPlaybackState.withPlayedAd(0, 1);
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED, AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void markCurrentGateAsSkippedForTheFirstGate() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(TEN_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED, AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void markCurrentGateAsSkippedForTheVeryLastPeriod() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(FORTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void markCurrentGateAsSkippedForTheVeryLastPeriodForLargeCurrentPosition() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(Long.MAX_VALUE, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE, AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void doesNotMarkAnyGateAsSkippedForNoAds() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(new ArrayList<AdvertBreak>(0)).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(0, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThat(initialAvailableAdPlaybackState).isEqualTo(adPlaybackState);
    }

    @Test
    public void doesNotMarkAnyGateAsSkippedForNegativePosition() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(-1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThat(initialAvailableAdPlaybackState).isEqualTo(adPlaybackState);
    }

    @Test
    public void doesNotMarkCurrentGateAsSkippedWhenNoPreviousGatesAvailable() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markCurrentGateAsSkipped(TEN_SECONDS_IN_MILLIS -1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThat(initialAvailableAdPlaybackState).isEqualTo(adPlaybackState);
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }

    private AdPlaybackState adPlaybackStateWithPlayedAdverts() {
        AdPlaybackState adPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        adPlaybackState = adPlaybackState.withPlayedAd(0, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(0, 1);
        adPlaybackState = adPlaybackState.withPlayedAd(1, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(2, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(3, 0);
        return adPlaybackState;
    }
}
