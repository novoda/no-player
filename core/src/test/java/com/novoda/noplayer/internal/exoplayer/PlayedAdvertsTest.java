package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.AdvertBreak;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_AVAILABLE;
import static com.google.android.exoplayer2.source.ads.AdPlaybackState.AD_STATE_PLAYED;
import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;

public class PlayedAdvertsTest {
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
    private static final AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK, FOURTH_ADVERT_BREAK));
    private static final List<AdvertBreak> ADVERT_BREAKS = advertPlaybackState.advertBreaks();
    private static final AdPlaybackState AD_PLAYBACK_STATE = advertPlaybackState.adPlaybackState();

    @Test
    public void doesNotMarkAsPlayed_whenCurrentPositionIsAtAdvertStartPosition() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(THIRTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        AdPlaybackState adPlaybackState = playedAdverts.adPlaybackState();

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void doesNotAddToPlayedAdvertBreaks_whenCurrentPositionIsAtAdvertStartPosition() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(THIRTY_SECONDS_IN_MILLIS, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        List<AdvertBreak> playedAdvertBreaks = playedAdverts.playedAdvertBreaks();

        assertThat(playedAdvertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK);
    }

    @Test
    public void marksAdvertsPriorToCurrentPositionAsPlayed() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(THIRTY_FIVE_SECONDS_IN_MILLIS, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        AdPlaybackState adPlaybackState = playedAdverts.adPlaybackState();

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_PLAYED});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void addsAdvertBreaksPriorToCurrentPositionToPlayedAdvertBreaks() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(THIRTY_FIVE_SECONDS_IN_MILLIS, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        List<AdvertBreak> playedAdvertBreaks = playedAdverts.playedAdvertBreaks();

        assertThat(playedAdvertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    }

    @Test
    public void marksNoAdvertsAsPlayed_whenPositionIsStart() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(BEGINNING, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        AdPlaybackState adPlaybackState = playedAdverts.adPlaybackState();

        assertThatGroupContains(adPlaybackState.adGroups[0], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[1], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[2], new int[]{AD_STATE_AVAILABLE});
        assertThatGroupContains(adPlaybackState.adGroups[3], new int[]{AD_STATE_AVAILABLE});
    }

    @Test
    public void addsNoAdvertBreaksToPlayedAdvertBreaks_whenPositionIsStart() {
        PlayedAdverts playedAdverts = PlayedAdverts.markAllPastAdvertsAsPlayed(BEGINNING, ADVERT_BREAKS, AD_PLAYBACK_STATE);
        List<AdvertBreak> playedAdvertBreaks = playedAdverts.playedAdvertBreaks();

        assertThat(playedAdvertBreaks).isEmpty();
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int[] states) {
        assertThat(adGroup.states).containsOnly(states);
    }
}
