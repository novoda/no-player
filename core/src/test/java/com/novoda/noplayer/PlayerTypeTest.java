package com.novoda.noplayer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PlayerTypeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void givenUnknownPlayerType_thenThrows() throws Exception {
        thrown.expect(PlayerType.UnknownPlayerTypeException.class);

        PlayerType.from("unknown player type 1234___");
    }
}
