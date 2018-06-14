package com.novoda.noplayer.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EitherTest {

    @Mock
    private Either.Consumer<String> leftConsumer;
    @Mock
    private Either.Consumer<Integer> rightConsumer;

    @Test
    public void givenEitherContainsLeft_whenApplyingConsumers_thenRunsLeftConsumerWithCorrectValue() {
        String value = "foo";
        Either<String, Integer> either = Either.left(value);

        either.apply(leftConsumer, rightConsumer);

        verify(leftConsumer).accept(value);
    }

    @Test
    public void givenEitherContainsRight_whenApplyingConsumers_thenRunsRightConsumerWithCorrectValue() {
        Integer value = 42;
        Either<String, Integer> either = Either.right(value);

        either.apply(leftConsumer, rightConsumer);

        verify(rightConsumer).accept(value);
    }
}
