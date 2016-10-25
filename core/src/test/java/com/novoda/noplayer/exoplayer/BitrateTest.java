package com.novoda.noplayer.exoplayer;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class BitrateTest {

    @Test
    public void givenBitrateFromBits_whenConvertingToKilobits_thenReturnExpected() {
        Bitrate bitrate = Bitrate.fromBitsPerSecond(10000);

        assertThat(bitrate.asKilobits()).isEqualTo(10);
    }
}
