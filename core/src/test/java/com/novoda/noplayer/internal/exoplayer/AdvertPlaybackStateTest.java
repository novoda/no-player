package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;

public class AdvertPlaybackStateTest {

    private static final int ONE_SECOND_IN_MICROS = 1000000;
    private static final int TWO_SECONDS_IN_MICROS = 2000000;
    private static final int THREE_SECONDS_IN_MICROS = 3000000;

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int TWO_SECONDS_IN_MILLIS = 2000;
    private static final int THREE_SECONDS_IN_MILLIS = 3000;

    private static final Advert FIRST_ADVERT = anAdvert()
            .withDurationInMillis(ONE_SECOND_IN_MILLIS)
            .build();
    private static final Advert SECOND_ADVERT = anAdvert()
            .withDurationInMillis(TWO_SECONDS_IN_MILLIS)
            .build();
    private static final Advert THIRD_ADVERT = anAdvert()
            .withDurationInMillis(THREE_SECONDS_IN_MILLIS)
            .build();

    private static final AdvertBreak FIRST_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(ONE_SECOND_IN_MILLIS)
            .withAdvert(FIRST_ADVERT)
            .build();
    private static final AdvertBreak SECOND_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TWO_SECONDS_IN_MILLIS)
            .withAdverts(FIRST_ADVERT, SECOND_ADVERT)
            .build();
    private static final AdvertBreak THIRD_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(THREE_SECONDS_IN_MILLIS)
            .withAdverts(FIRST_ADVERT, SECOND_ADVERT, THIRD_ADVERT)
            .build();

    @Test
    public void createsCorrectAdvertPlaybackState() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroupCount).isEqualTo(3);
        assertThat(adPlaybackState.adGroupTimesUs).containsSequence(ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS);
        assertThatGroupContains(adPlaybackState.adGroups[0], 1, new long[]{ONE_SECOND_IN_MICROS}, new Uri[]{FIRST_ADVERT.uri()});
        assertThatGroupContains(adPlaybackState.adGroups[1], 2, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS}, new Uri[]{FIRST_ADVERT.uri(), SECOND_ADVERT.uri()});
        assertThatGroupContains(adPlaybackState.adGroups[2], 3, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS}, new Uri[]{FIRST_ADVERT.uri(), SECOND_ADVERT.uri(), THIRD_ADVERT.uri()});
    }

    @Test
    public void advertBreaksAreReorderedBasedOnStartTime() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        List<AdvertBreak> actualAdvertBreaks = advertPlaybackState.advertBreaks();

        assertThat(actualAdvertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    }

    @Test
    public void doesNotChangeTheOrderOfTheInputParameter() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState.from(advertBreaks);

        assertThat(advertBreaks).containsExactly(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int numberOfAdverts, long[] advertDurations, Uri[] advertUris) {
        assertThat(adGroup.count).isEqualTo(numberOfAdverts);
        assertThat(adGroup.durationsUs).containsSequence(advertDurations);
        assertThat(adGroup.uris).containsExactly(advertUris);
    }
}
