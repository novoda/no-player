package com.novoda.noplayer.internal.exoplayer;

import android.os.Handler;

import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertView;
import com.novoda.noplayer.AdvertsLoader;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import utils.ExceptionMatcher;

import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class NoPlayerAdsLoaderTest {

    private static final AdvertBreak FIRST_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(1000)
            .withAdvertBreakId("first_break_id")
            .withAdvert(anAdvert().withDurationInMillis(1000).build())
            .withAdvert(anAdvert().withDurationInMillis(2000).build())
            .withAdvert(anAdvert().withDurationInMillis(3000).build())
            .withAdvert(anAdvert().withDurationInMillis(4000).build())
            .build();
    private static final AdvertBreak SECOND_ADVERT_BREAK = anAdvertBreak()
            .withStartTimeInMillis(2000)
            .withAdvertBreakId("second_break_id")
            .withAdvert(anAdvert().withDurationInMillis(4000).build())
            .withAdvert(anAdvert().withDurationInMillis(5000).build())
            .withAdvert(anAdvert().withDurationInMillis(6000).build())
            .build();
    private final AdvertsLoader advertsLoader = mock(AdvertsLoader.class);
    private final Handler handler = mock(Handler.class);

    private final AdvertView advertView = mock(AdvertView.class);
    private final AdsLoader.EventListener eventListener = mock(AdsLoader.EventListener.class);
    private final AdsLoader.AdViewProvider adViewProvider = mock(AdsLoader.AdViewProvider.class);

    private final NoPlayerAdsLoader noPlayerAdsLoader = new NoPlayerAdsLoader(advertsLoader, handler);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        given(handler.post(any(Runnable.class))).willAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();
                return null;
            }
        });
    }

    @Test
    public void notifiesEventListenerWhenAdvertLoadingFails() {
        noPlayerAdsLoader.start(eventListener, adViewProvider);

        invokeCallback().onAdvertsError("some error");

        then(eventListener).should().onAdLoadError(null, null);
    }

    @Test
    public void notifiesAdvertViewWhenAdvertLoadingSucceeds() {
        noPlayerAdsLoader.attach(advertView);

        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        then(advertView).should().setup(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK), noPlayerAdsLoader);
    }

    @Test
    public void doesNothingWhenAdvertViewIsNotAttachedAndAdvertLoadingSucceeds() {
        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        then(advertView).should(never()).setup(ArgumentMatchers.<AdvertBreak>anyList(), any(AdvertView.AdvertInteractionListener.class));
    }

    @Test
    public void doesNothingWhenAdvertViewIsDetachedWhenAdvertLoadingSucceeds() {
        noPlayerAdsLoader.attach(advertView);
        noPlayerAdsLoader.detach(advertView);

        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        then(advertView).should(never()).setup(ArgumentMatchers.<AdvertBreak>anyList(), any(AdvertView.AdvertInteractionListener.class));
    }

    @Test
    public void throwsWhenAdvertGroupIndexHigherThanCachedGroups() {
        thrown.expect(ExceptionMatcher.matches("Advert is being played but no data about advert breaks is cached.", IllegalStateException.class));

        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        noPlayerAdsLoader.advertDurationBy(2, 0);
    }

    @Test
    public void throwsWhenAdvertIndexInAdvertGroupIndexHigherThanNumberOfAdvertsInGroup() {
        thrown.expect(ExceptionMatcher.matches("Cached advert break data contains less adverts than current index.", IllegalStateException.class));

        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        noPlayerAdsLoader.advertDurationBy(0, FIRST_ADVERT_BREAK.adverts().size());
    }

    @Test
    public void returnsAdvertLengthInMicroseconds() {
        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK));

        long advertDurationMicros = noPlayerAdsLoader.advertDurationBy(0, 0);

        assertThat(advertDurationMicros).isEqualTo(1000000);
    }

    @Test
    public void returnsAdvertLengthInMicrosecondsUsingAdvertBreaksOrderedByStartTime() {
        noPlayerAdsLoader.start(eventListener, adViewProvider);
        invokeCallback().onAdvertsLoaded(Arrays.asList(SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK));

        long advertDurationMicros = noPlayerAdsLoader.advertDurationBy(0, 0);

        assertThat(advertDurationMicros).isEqualTo(1000000);
    }

    private AdvertsLoader.Callback invokeCallback() {
        ArgumentCaptor<AdvertsLoader.Callback> listenerCaptor = ArgumentCaptor.forClass(AdvertsLoader.Callback.class);
        verify(advertsLoader).load(listenerCaptor.capture());
        return listenerCaptor.getValue();
    }
}
