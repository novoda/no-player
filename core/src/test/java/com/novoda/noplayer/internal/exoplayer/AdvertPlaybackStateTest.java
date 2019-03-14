package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AdvertPlaybackStateTest {

    private static final int ONE_SECOND_IN_MICROS = 1000000;
    private static final int TWO_SECONDS_IN_MICROS = 2000000;
    private static final int THREE_SECONDS_IN_MICROS = 3000000;

    private static final Uri FIRST_URI = mock(Uri.class);
    private static final Uri SECOND_URI = mock(Uri.class);
    private static final Uri THIRD_URI = mock(Uri.class);

    private static final Advert FIRST_ADVERT = new Advert(ONE_SECOND_IN_MICROS, FIRST_URI);
    private static final Advert SECOND_ADVERT = new Advert(TWO_SECONDS_IN_MICROS, SECOND_URI);
    private static final Advert THIRD_ADVERT = new Advert(THREE_SECONDS_IN_MICROS, THIRD_URI);

    private static final AdvertBreak FIRST_ADVERT_BREAK = new AdvertBreak(
            ONE_SECOND_IN_MICROS, Collections.singletonList(FIRST_ADVERT)
    );

    private static final AdvertBreak SECOND_ADVERT_BREAK = new AdvertBreak(
            TWO_SECONDS_IN_MICROS, Arrays.asList(FIRST_ADVERT, SECOND_ADVERT)
    );

    private static final AdvertBreak THIRD_ADVERT_BREAK = new AdvertBreak(
            THREE_SECONDS_IN_MICROS, Arrays.asList(FIRST_ADVERT, SECOND_ADVERT, THIRD_ADVERT)
    );

    @Test
    public void createsCorrectAdvertPlaybackState() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdPlaybackState adPlaybackState = AdvertPlaybackState.from(advertBreaks);

        assertThat(adPlaybackState.adGroupCount).isEqualTo(3);
        assertThat(adPlaybackState.adGroupTimesUs).containsSequence(ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS);
        assertThatGroupContains(adPlaybackState.adGroups[0], 1, new long[]{ONE_SECOND_IN_MICROS}, new Uri[]{FIRST_URI});
        assertThatGroupContains(adPlaybackState.adGroups[1], 2, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS}, new Uri[]{FIRST_URI, SECOND_URI});
        assertThatGroupContains(adPlaybackState.adGroups[2], 3, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS}, new Uri[]{FIRST_URI, SECOND_URI, THIRD_URI});
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int numberOfAdverts, long[] advertDurations, Uri[] advertUris) {
        assertThat(adGroup.count).isEqualTo(numberOfAdverts);
        assertThat(adGroup.durationsUs).containsSequence(advertDurations);
        assertThat(adGroup.uris).containsExactly(advertUris);
    }
}