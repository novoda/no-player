package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static com.google.android.exoplayer2.source.ads.AdPlaybackState.*;
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
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllNonPlayedAdvertsAsSkipped(ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void skipAdvertBreak() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllNonPlayedAdvertsAsSkipped(ADVERT_BREAKS.get(0), ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotSkipAdvertThatHasAlreadyPlayed() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        initialAvailableAdPlaybackState = initialAvailableAdPlaybackState.withPlayedAd(1, 0);
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllNonPlayedAdvertsAsSkipped(ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    @Test
    public void makesAvailableAdvertsBeforeCurrentPositionSkipped() {
        AdPlaybackState initialAvailableAdPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllPastAvailableAdvertsAsSkipped(TWENTY_SECONDS_IN_MILLIS + 1, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotMarkAdvertsAlreadyPlayedAsSkipped() {
        AdPlaybackState initialAvailableAdPlaybackState = adPlaybackStateWithPlayedAdverts();
        AdPlaybackState adPlaybackState = SkippedAdverts.markAllPastAvailableAdvertsAsSkipped(TWENTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, initialAvailableAdPlaybackState);

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_PLAYED});
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }

    private AdPlaybackState adPlaybackStateWithPlayedAdverts() {
        AdPlaybackState adPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS).adPlaybackState();
        adPlaybackState = adPlaybackState.withPlayedAd(0, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(1, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(2, 0);
        adPlaybackState = adPlaybackState.withPlayedAd(3, 0);
        return adPlaybackState;
    }
}
