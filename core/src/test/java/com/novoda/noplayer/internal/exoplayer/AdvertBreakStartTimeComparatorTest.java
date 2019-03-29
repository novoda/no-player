package com.novoda.noplayer.internal.exoplayer;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertBreakId;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AdvertBreakStartTimeComparatorTest {

    private static final AdvertBreakId FIRST_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_one");
    private static final AdvertBreakId SECOND_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_two");
    private static final AdvertBreakId THIRD_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_three");

    private static final AdvertBreak FIRST_ADVERT_BREAK = new AdvertBreak(
            FIRST_ADVERT_BREAK_ID, 10000, Collections.<Advert>emptyList()
    );

    private static final AdvertBreak SECOND_ADVERT_BREAK = new AdvertBreak(
            SECOND_ADVERT_BREAK_ID, 20000, Collections.<Advert>emptyList()
    );

    private static final AdvertBreak THIRD_ADVERT_BREAK = new AdvertBreak(
            THIRD_ADVERT_BREAK_ID, 30000, Collections.<Advert>emptyList()
    );

    @Test
    public void sortsBasedOnStartTime() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);
        Collections.sort(advertBreaks, new AdvertBreakStartTimeComparator());

        assertThat(advertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    }
}
