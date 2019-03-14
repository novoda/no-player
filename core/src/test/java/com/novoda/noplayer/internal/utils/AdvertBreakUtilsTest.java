package com.novoda.noplayer.internal.utils;

import android.net.Uri;

import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AdvertBreakUtilsTest {

    private static final int ONE_SECOND_IN_MICROS = 1000000;
    private static final int TWO_SECONDS_IN_MICROS = 2000000;
    private static final int THREE_SECONDS_IN_MICROS = 3000000;

    private static final Advert FIRST_ADVERT = new Advert(ONE_SECOND_IN_MICROS, mock(Uri.class));
    private static final Advert SECOND_ADVERT = new Advert(TWO_SECONDS_IN_MICROS, mock(Uri.class));
    private static final Advert THIRD_ADVERT = new Advert(THREE_SECONDS_IN_MICROS, mock(Uri.class));

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
    public void extractsAdvertBreakOffsets() {
        List<AdvertBreak> advertBreaks = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);

        long[] advertOffsets = AdvertBreakUtils.advertOffsets(advertBreaks);

        assertThat(advertOffsets).containsOnly(ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS);
    }

    @Test
    public void extractsAdvertBreakDurations() {
        List<AdvertBreak> advertBreaks = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);

        long[][] advertBreakDurations = AdvertBreakUtils.advertBreakDurations(advertBreaks);

        assertThat(advertBreakDurations).isEqualTo(new long[][]{
                {ONE_SECOND_IN_MICROS},
                {ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS},
                {ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS}
        });
    }

    @Test
    public void extractsTheNumberOfAdvertsPerBreak() {
        List<AdvertBreak> advertBreaks = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);

        int[] numberOfAdvertsPerBreak = AdvertBreakUtils.numberOfAdvertsPerAdvertBreak(advertBreaks);

        assertThat(numberOfAdvertsPerBreak).containsSequence(1, 2, 3);
    }
}
