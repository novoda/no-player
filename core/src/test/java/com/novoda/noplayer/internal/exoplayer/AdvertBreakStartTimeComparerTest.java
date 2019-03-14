package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AdvertBreakStartTimeComparerTest {

    private static final AdvertBreak FIRST_ADVERT_BREAK = new AdvertBreak(
            10000, Collections.<Advert>emptyList()
    );

    private static final AdvertBreak SECOND_ADVERT_BREAK = new AdvertBreak(
            20000, Collections.<Advert>emptyList()
    );

    private static final AdvertBreak THIRD_ADVERT_BREAK = new AdvertBreak(
            20000, Collections.<Advert>emptyList()
    );

    @Test
    public void sortsBasedOnStartTime() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);
        Collections.sort(advertBreaks, new AdvertBreakStartTimeComparer());

        assertThat(advertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    }
}
