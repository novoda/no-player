package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static com.novoda.noplayer.internal.exoplayer.AdGroupFixture.anAdGroup;
import static org.fest.assertions.api.Assertions.assertThat;

public class AdvertPlaybackStateTest {

    private static final int HALF_SECOND_IN_MICROS = 500000;
    private static final int ONE_SECOND_IN_MICROS = 1000000;
    private static final int TWO_SECONDS_IN_MICROS = 2000000;
    private static final int THREE_SECONDS_IN_MICROS = 3000000;

    private static final int HALF_SECOND_IN_MILLIS = 500;
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
    private static final AdPlaybackState.AdGroup FIRST_AD_GROUP = firstAdGroupFixture().build();
    private static final AdPlaybackState.AdGroup SECOND_AD_GROUP = secondAdGroupFixture().build();
    private static final AdPlaybackState.AdGroup THIRD_AD_GROUP = thirdAdGroupFixture().build();

    @Test
    public void createsCorrectAdvertPlaybackStateWithZeroResumePosition() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroupCount).isEqualTo(3);
        assertThat(adPlaybackState.adGroupTimesUs).containsSequence(ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS);
        assertThat(adPlaybackState.adGroups).containsExactly(FIRST_AD_GROUP, SECOND_AD_GROUP, THIRD_AD_GROUP);
        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(0L);
        assertThat(adPlaybackState.contentDurationUs).isEqualTo(C.TIME_UNSET);
    }

    @Test
    public void createsEmptyStateWhenAdvertBreaksAreEmpty() {
        List<AdvertBreak> advertBreaks = Collections.emptyList();

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks, HALF_SECOND_IN_MILLIS);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroupCount).isEqualTo(0);
        assertThat(adPlaybackState.adGroupTimesUs).isEmpty();
        assertThat(adPlaybackState.adGroups).isEmpty();
        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(0L);
        assertThat(adPlaybackState.contentDurationUs).isEqualTo(C.TIME_UNSET);
    }

    @Test
    public void createsAdvertPlaybackStateWithResumePositionInMicroseconds() {
        List<AdvertBreak> advertBreaks = Collections.singletonList(FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks, HALF_SECOND_IN_MILLIS);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(HALF_SECOND_IN_MICROS);
    }

    @Test
    public void marksAdvertsInAdvertBreakAsPlayedWhenResumePositionIsMoreThanEachAdvertDuration() {
        List<AdvertBreak> advertBreaks = Collections.singletonList(THIRD_ADVERT_BREAK);
        AdPlaybackState.AdGroup expectedAdGroup = thirdAdGroupFixture()
                .withPlayedStateAt(0)
                .withPlayedStateAt(1)
                .build();
        long resumePosition = FIRST_ADVERT.durationInMillis() + SECOND_ADVERT.durationInMillis() + HALF_SECOND_IN_MILLIS;

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks, resumePosition);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroups).containsExactly(expectedAdGroup);
        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(HALF_SECOND_IN_MICROS);
    }

    @Test
    public void marksAllAdvertsInAdvertBreakPlayedWhenResumePositionIsBiggerThanTotalLengthOfFirstAdvertBreak() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK);
        AdPlaybackState.AdGroup expectedAdGroup = secondAdGroupFixture()
                .withPlayedStateAt(0)
                .withPlayedStateAt(1)
                .build();

        int resumePosition = TWO_SECONDS_IN_MILLIS + THREE_SECONDS_IN_MILLIS;
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks, resumePosition);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroups).containsExactly(expectedAdGroup, THIRD_AD_GROUP);
    }

    @Test
    public void doesNotSetResumePositionWhenBiggerThanTotalLengthOfFirstAdvertBreak() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK);

        int resumePosition = THREE_SECONDS_IN_MILLIS * 2;
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks, resumePosition);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(0);
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

    private static AdGroupFixture firstAdGroupFixture() {
        return anAdGroup()
                .withAdCount(1)
                .withAdDurationsUs(new long[]{ONE_SECOND_IN_MICROS})
                .withAdUris(new Uri[]{FIRST_ADVERT.uri()});
    }

    private static AdGroupFixture secondAdGroupFixture() {
        return anAdGroup()
                .withAdCount(2)
                .withAdDurationsUs(new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS})
                .withAdUris(new Uri[]{FIRST_ADVERT.uri(), SECOND_ADVERT.uri()});
    }

    private static AdGroupFixture thirdAdGroupFixture() {
        return anAdGroup()
                .withAdCount(3)
                .withAdDurationsUs(new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS})
                .withAdUris(new Uri[]{FIRST_ADVERT.uri(), SECOND_ADVERT.uri(), THIRD_ADVERT.uri()});
    }
}
