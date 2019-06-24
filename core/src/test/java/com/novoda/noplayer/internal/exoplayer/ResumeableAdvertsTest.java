package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static com.novoda.noplayer.internal.exoplayer.AdGroupFixture.anAdGroup;
import static org.fest.assertions.api.Assertions.assertThat;

public class ResumeableAdvertsTest {

    private static final int ZERO = 0;
    private static final int FIVE_SECONDS_IN_MICROS = 5000000;
    private static final int TEN_SECONDS_IN_MICROS = 10000000;
    private static final int TWENTY_SECONDS_IN_MICROS = 20000000;

    private static final int HALF_SECOND_IN_MILLIS = 500;
    private static final int FIVE_SECONDS_IN_MILLIS = 5000;
    private static final int TEN_SECONDS_IN_MILLIS = 10000;
    private static final int TWENTY_SECONDS_IN_MILLIS = 20000;
    private static final int THIRTY_SECONDS_IN_MILLIS = 30000;

    private static final Advert FIRST_ADVERT = anAdvert()
            .withDurationInMillis(TWENTY_SECONDS_IN_MILLIS)
            .build();
    private static final Advert SECOND_ADVERT = anAdvert()
            .withDurationInMillis(FIVE_SECONDS_IN_MILLIS)
            .build();
    private static final Advert THIRD_ADVERT = anAdvert()
            .withDurationInMillis(TEN_SECONDS_IN_MILLIS)
            .build();

    private static final AdvertBreak FIRST_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TEN_SECONDS_IN_MILLIS)
            .withAdverts(THIRD_ADVERT)
            .build();
    private static final AdvertBreak SECOND_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(TWENTY_SECONDS_IN_MILLIS)
            .withAdverts(FIRST_ADVERT, SECOND_ADVERT)
            .build();
    private static final List<AdvertBreak> ADVERT_BREAKS = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK);
    private static final AdPlaybackState.AdGroup FIRST_AD_GROUP = firstAdGroupFixture().build();

    @Test
    public void createsAdvertPlaybackStateWithResumePositionInMicroseconds() {
        List<AdvertBreak> advertBreaks = Collections.singletonList(SECOND_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        AdPlaybackState adPlaybackState = ResumeableAdverts.markAsResumeableFrom(advertPlaybackState.adPlaybackState(), TEN_SECONDS_IN_MILLIS, TWENTY_SECONDS_IN_MILLIS);

        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(TEN_SECONDS_IN_MICROS);
    }

    @Test
    public void marksAdvertsInAdvertBreakAsPlayedWhenResumePositionIsMoreThanEachAdvertDuration() {
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS);
        long resumePosition = FIRST_ADVERT.durationInMillis() + SECOND_ADVERT.durationInMillis() + HALF_SECOND_IN_MILLIS;
        AdPlaybackState adPlaybackState = ResumeableAdverts.markAsResumeableFrom(advertPlaybackState.adPlaybackState(), resumePosition, TWENTY_SECONDS_IN_MILLIS);

        AdPlaybackState.AdGroup expectedAdGroup = secondAdGroupFixture()
                .withPlayedStateAt(0)
                .withPlayedStateAt(1)
                .build();

        assertThat(adPlaybackState.adGroups).containsExactly(FIRST_AD_GROUP, expectedAdGroup);
        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(ZERO);
    }

    @Test
    public void marksAllAdvertsInAdvertBreakPlayedWhenResumePositionIsBiggerThanTotalLengthOfFirstAdvertBreak() {
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS);
        AdPlaybackState adPlaybackState = ResumeableAdverts.markAsResumeableFrom(
                advertPlaybackState.adPlaybackState(),
                THIRTY_SECONDS_IN_MILLIS + HALF_SECOND_IN_MILLIS,
                TWENTY_SECONDS_IN_MILLIS
        );

        AdPlaybackState.AdGroup expectedAdGroup = secondAdGroupFixture()
                .withPlayedStateAt(0)
                .withPlayedStateAt(1)
                .build();
        assertThat(adPlaybackState.adGroups).containsExactly(FIRST_AD_GROUP, expectedAdGroup);
    }

    @Test
    public void marksAllAdvertsInAdvertBreakPlayedWhenResumePositionIsSameASTotalLengthOfFirstAdvertBreak() {
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS);
        AdPlaybackState adPlaybackState = ResumeableAdverts.markAsResumeableFrom(
                advertPlaybackState.adPlaybackState(),
                THIRTY_SECONDS_IN_MILLIS,
                TWENTY_SECONDS_IN_MILLIS
        );

        AdPlaybackState.AdGroup expectedAdGroup = secondAdGroupFixture()
                .withPlayedStateAt(0)
                .withPlayedStateAt(1)
                .build();
        assertThat(adPlaybackState.adGroups).containsExactly(FIRST_AD_GROUP, expectedAdGroup);
    }

    @Test
    public void doesNotSetResumePositionWhenBiggerThanTotalLengthOfFirstAdvertBreak() {
        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(ADVERT_BREAKS);
        AdPlaybackState adPlaybackState = ResumeableAdverts.markAsResumeableFrom(
                advertPlaybackState.adPlaybackState(),
                THIRTY_SECONDS_IN_MILLIS + HALF_SECOND_IN_MILLIS,
                TWENTY_SECONDS_IN_MILLIS
        );

        assertThat(adPlaybackState.adResumePositionUs).isEqualTo(0);
    }

    private static AdGroupFixture firstAdGroupFixture() {
        return anAdGroup()
                .withAdCount(1)
                .withAdDurationsUs(new long[]{TEN_SECONDS_IN_MICROS})
                .withAdUris(new Uri[]{THIRD_ADVERT.uri()});
    }

    private static AdGroupFixture secondAdGroupFixture() {
        return anAdGroup()
                .withAdCount(2)
                .withAdDurationsUs(new long[]{TWENTY_SECONDS_IN_MICROS, FIVE_SECONDS_IN_MICROS})
                .withAdUris(new Uri[]{FIRST_ADVERT.uri(), SECOND_ADVERT.uri()});
    }

}
