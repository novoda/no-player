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
import static com.novoda.noplayer.internal.exoplayer.AdvertPlaybackState.AdvertBreakState.AVAILABLE;
import static com.novoda.noplayer.internal.exoplayer.AdvertPlaybackState.AdvertBreakState.PLAYED;
import static com.novoda.noplayer.internal.exoplayer.AdvertPlaybackState.AdvertBreakState.SKIPPED;
import static org.fest.assertions.api.Assertions.assertThat;

public class AdvertPlaybackStateByStateTest {

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

    private static final AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK, FOURTH_ADVERT_BREAK));
    private static final List<AdvertBreak> ADVERT_BREAKS = advertPlaybackState.advertBreaks();
    private static final AdPlaybackState AD_PLAYBACK_STATE = advertPlaybackState.adPlaybackState();

    @Test
    public void doesNotAlterAdPlaybackStateWhenAdvertsAreAvailable() {
        List<AdvertPlaybackState.AdvertBreakState> advertBreakStates = Arrays.asList(AVAILABLE, AVAILABLE, AVAILABLE, AVAILABLE);

        AdPlaybackState modifiedAdPlaybackState = AdvertPlaybackStateByState.from(AD_PLAYBACK_STATE, ADVERT_BREAKS, advertBreakStates);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void marksPlayedAdverts() {
        List<AdvertPlaybackState.AdvertBreakState> advertBreakStates = Arrays.asList(AVAILABLE, PLAYED, PLAYED, AVAILABLE);

        AdPlaybackState modifiedAdPlaybackState = AdvertPlaybackStateByState.from(AD_PLAYBACK_STATE, ADVERT_BREAKS, advertBreakStates);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void marksSkippedAdverts() {
        List<AdvertPlaybackState.AdvertBreakState> advertBreakStates = Arrays.asList(AVAILABLE, SKIPPED, AVAILABLE, SKIPPED);

        AdPlaybackState modifiedAdPlaybackState = AdvertPlaybackStateByState.from(AD_PLAYBACK_STATE, ADVERT_BREAKS, advertBreakStates);

        assertThatGroupContains(modifiedAdPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[1], new int[]{AD_STATE_SKIPPED});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(modifiedAdPlaybackState.adGroups[3], new int[]{AD_STATE_SKIPPED});
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }

}
