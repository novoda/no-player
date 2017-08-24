package com.novoda.noplayer.internal.exoplayer.mediasource;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AudioTrackTypeTest {

    private static final int MAIN_SELECTION_FLAG = 1;
    private static final int ALTERNATIVE_SELECTION_FLAG = 0;
    private static final int RANDOM_SELECTION_FLAG = 2;

    @Test
    public void givenSelectionFlagIsZero_whenCreatingAudioTrackType_thenReturnsAlternative() {
        AudioTrackType audioTrackType = AudioTrackType.from(ALTERNATIVE_SELECTION_FLAG);

        assertThat(audioTrackType).isEqualTo(AudioTrackType.ALTERNATIVE);
    }

    @Test
    public void givenSelectionFlagIsOne_whenCreatingAudioTrackType_thenReturnsMain() {
        AudioTrackType audioTrackType = AudioTrackType.from(MAIN_SELECTION_FLAG);

        assertThat(audioTrackType).isEqualTo(AudioTrackType.MAIN);
    }

    @Test
    public void givenAnyOtherSelectionFlag_whenCreatingAudioTrackType_thenReturnsUnknown() {
        AudioTrackType audioTrackType = AudioTrackType.from(RANDOM_SELECTION_FLAG);

        assertThat(audioTrackType).isEqualTo(AudioTrackType.UNKNOWN);
    }
}
