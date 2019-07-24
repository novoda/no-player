package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.C;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.NoPlayer;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mockito;

import utils.ExceptionMatcher;

import static com.novoda.noplayer.AdvertBreakFixtures.anAdvertBreak;
import static com.novoda.noplayer.AdvertFixtures.anAdvert;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class AdvertStateTest {

    private static final boolean IS_NOT_PLAYING_ADVERT = false;
    private static final boolean IS_PLAYING_ADVERT = true;
    private static final int UNSET_ADVERT_BREAK_INDEX = -1;
    private static final int UNSET_ADVERT_INDEX = -1;
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final List<AdvertBreak> ADVERT_BREAKS = Arrays.asList(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    private final NoPlayer.AdvertListener advertListener = mock(NoPlayer.AdvertListener.class);
    private final AdvertState.Callback callback = mock(AdvertState.Callback.class);

    private final AdvertState advertState = new AdvertState(
            ADVERT_BREAKS,
            advertListener,
            callback
    );

    @Test
    public void emitsNoEvents_whenNotTransitioningToAdverts() {
        advertState.update(IS_NOT_PLAYING_ADVERT, UNSET_ADVERT_BREAK_INDEX, UNSET_ADVERT_INDEX);

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void notifiesStartOfAdvertBreak_andAdvert_whenTransitioningToAdverts() {
        advertState.update(IS_PLAYING_ADVERT, 0, 0);

        InOrder inOrder = Mockito.inOrder(advertListener);
        then(advertListener).should(inOrder).onAdvertBreakStart(FIRST_ADVERT_BREAK);
        then(advertListener).should(inOrder).onAdvertStart(FIRST_ADVERT);
        then(advertListener).shouldHaveNoMoreInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void emitsNoAdditionalEvents_whenCallingWithTheSameIndices() {
        advertState.update(IS_PLAYING_ADVERT, 0, 0);
        Mockito.reset(advertListener, callback);
        advertState.update(IS_PLAYING_ADVERT, 0, 0);

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void notifiesEndOfAdvertBreak_andAdvert_whenTransitioningToContent() {
        advertState.update(IS_PLAYING_ADVERT, 0, 0);
        Mockito.reset(advertListener, callback);
        advertState.update(IS_NOT_PLAYING_ADVERT, UNSET_ADVERT_BREAK_INDEX, UNSET_ADVERT_INDEX);

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertPlayed(0, 0);
        then(advertListener).should(inOrder).onAdvertEnd(FIRST_ADVERT);
        then(advertListener).should(inOrder).onAdvertBreakEnd(FIRST_ADVERT_BREAK);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void transitionsBetweenAdverts() {
        advertState.update(IS_PLAYING_ADVERT, 1, 0);
        Mockito.reset(advertListener, callback);
        advertState.update(IS_PLAYING_ADVERT, 1, 1);

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertPlayed(1, 0);
        then(advertListener).should(inOrder).onAdvertEnd(FIRST_ADVERT);
        then(advertListener).should(inOrder).onAdvertStart(SECOND_ADVERT);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void disablesAdverts() {
        advertState.disableAdverts();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertsDisabled();
        then(advertListener).should(inOrder).onAdvertsDisabled();
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void doesNotDisableAdverts_whenAlreadyDisabled() {
        advertState.disableAdverts();
        Mockito.reset(advertListener, callback);
        advertState.disableAdverts();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void enablesAdverts() {
        advertState.disableAdverts();
        Mockito.reset(advertListener, callback);
        advertState.enableAdverts();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertsEnabled();
        then(advertListener).should(inOrder).onAdvertsEnabled(ADVERT_BREAKS);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void doesNotEnableAdverts_whenAlreadyEnabled() {
        advertState.enableAdverts();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotSkipAdvertBreak_whenDisabled() {
        advertState.disableAdverts();
        Mockito.reset(advertListener, callback);
        advertState.skipAdvertBreak();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotSkipAdvertBreak_whenNotPlaying() {
        advertState.skipAdvertBreak();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotSkipAdvertBreak_whenPlayingContent() {
        advertState.update(false, UNSET_ADVERT_BREAK_INDEX, UNSET_ADVERT_INDEX);
        Mockito.reset(advertListener, callback);
        advertState.skipAdvertBreak();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void skipsAdvertBreak_whenPlayingAdverts() {
        advertState.update(true, 0, 0);
        Mockito.reset(advertListener, callback);
        advertState.skipAdvertBreak();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertBreakSkipped(0);
        then(advertListener).should(inOrder).onAdvertBreakSkipped(FIRST_ADVERT_BREAK);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void doesNotSkipAdvert_whenDisabled() {
        advertState.disableAdverts();
        Mockito.reset(advertListener, callback);
        advertState.skipAdvert();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotSkipAdvert_whenNotPlaying() {
        advertState.skipAdvert();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotSkipAdvert_whenPlayingContent() {
        advertState.update(false, UNSET_ADVERT_BREAK_INDEX, UNSET_ADVERT_INDEX);
        Mockito.reset(advertListener, callback);
        advertState.skipAdvert();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void skipsAdvert_whenPlayingAdverts() {
        advertState.update(true, 1, 0);
        Mockito.reset(advertListener, callback);
        advertState.skipAdvert();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertSkipped(1, 0);
        then(advertListener).should(inOrder).onAdvertSkipped(FIRST_ADVERT);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void notifiesBreakEnd_whenSkippingLastAdvertInBreak() {
        advertState.update(true, 1, 1);
        Mockito.reset(advertListener, callback);
        advertState.skipAdvert();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(callback).should(inOrder).onAdvertSkipped(1, 1);
        then(advertListener).should(inOrder).onAdvertSkipped(SECOND_ADVERT);
        then(advertListener).should(inOrder).onAdvertBreakEnd(SECOND_ADVERT_BREAK);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void doesNotClickAdvert_whenPlayingContent() {
        advertState.update(false, UNSET_ADVERT_BREAK_INDEX, UNSET_ADVERT_INDEX);
        Mockito.reset(advertListener, callback);
        advertState.clickAdvert();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void doesNotClickAdvert_whenNotPlaying() {
        advertState.clickAdvert();

        then(advertListener).shouldHaveZeroInteractions();
        then(callback).shouldHaveZeroInteractions();
    }

    @Test
    public void clicksAdvert_whenPlayingAdverts() {
        advertState.update(true, 0, 0);
        Mockito.reset(advertListener, callback);
        advertState.clickAdvert();

        InOrder inOrder = Mockito.inOrder(advertListener, callback);
        then(advertListener).should(inOrder).onAdvertClicked(FIRST_ADVERT);
        then(callback).shouldHaveNoMoreInteractions();
        then(advertListener).shouldHaveNoMoreInteractions();
    }

    @Test
    public void advertDurationThrows_whenAdvertBreakIndexIsInvalid() {
        thrown.expect(ExceptionMatcher.matches("Advert is being played but no data about advert breaks is cached.", IllegalStateException.class));

        advertState.advertDurationBy(3, 0);
    }

    @Test
    public void advertDurationThrows_whenAdvertIndexIsInvalid() {
        thrown.expect(ExceptionMatcher.matches("Cached advert break data contains less adverts than current index.", IllegalStateException.class));

        advertState.advertDurationBy(0, 1);
    }

    @Test
    public void returnsAdvertDurationInMicros() {
        long advertDuration = advertState.advertDurationBy(0, 0);

        assertThat(advertDuration).isEqualTo(C.msToUs(ONE_SECOND_IN_MILLIS));
    }
}
