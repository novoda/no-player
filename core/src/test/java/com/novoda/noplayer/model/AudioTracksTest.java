package com.novoda.noplayer.model;

import com.novoda.noplayer.internal.exoplayer.mediasource.AudioTrackType;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AudioTracksTest {

    private static final PlayerAudioTrack MAIN_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().withAudioTrackType(AudioTrackType.MAIN).build();
    private static final PlayerAudioTrack ALTERNATIVE_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().withAudioTrackType(AudioTrackType.ALTERNATIVE).build();
    private static final int FIRST_INDEX = 0;
    private static final int EXPECTED_SIZE = 2;

    @Test
    public void givenAudioTracks_withAlternativeTrack_whenCheckingContainsAlternativeTrack_thenReturnsTrue() {
        AudioTracks audioTracks = AudioTracks.from(Arrays.asList(MAIN_TRACK, ALTERNATIVE_TRACK));

        boolean containsAlternativeTrack = audioTracks.containsTrackWith(AudioTrackType.ALTERNATIVE);

        assertThat(containsAlternativeTrack).isTrue();
    }

    @Test
    public void givenAudioTracks_withoutAlternativeTrack_whenCheckingContainsAlternativeTrack_thenReturnsFalse() {
        AudioTracks audioTracks = AudioTracks.from(Collections.singletonList(MAIN_TRACK));

        boolean containsAlternativeTrack = audioTracks.containsTrackWith(AudioTrackType.ALTERNATIVE);

        assertThat(containsAlternativeTrack).isFalse();
    }

    @Test
    public void givenAudioTracks_whenGettingTrack_thenReturnsTrack() {
        AudioTracks audioTracks = AudioTracks.from(Collections.singletonList(MAIN_TRACK));

        PlayerAudioTrack playerAudioTrack = audioTracks.get(FIRST_INDEX);

        assertThat(playerAudioTrack).isEqualTo(MAIN_TRACK);
    }

    @Test
    public void givenAudioTracks_whenGettingSize_thenReturnsSize() {
        AudioTracks audioTracks = AudioTracks.from(Arrays.asList(MAIN_TRACK, ALTERNATIVE_TRACK));

        int size = audioTracks.size();

        assertThat(size).isEqualTo(EXPECTED_SIZE);
    }
}
