package com.novoda.noplayer.model;

import com.novoda.noplayer.internal.exoplayer.mediasource.AudioTrackType;
import com.novoda.utils.Optional;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AudioTracksTest {

    private static final PlayerAudioTrack MAIN_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().withAudioTrackType(AudioTrackType.MAIN).build();
    private static final PlayerAudioTrack AUDIO_DESCRIBED_TRACK = PlayerAudioTrackFixture.aPlayerAudioTrack().withAudioTrackType(AudioTrackType.AUDIO_DESCRIBED).build();
    private static final int FIRST_INDEX = 0;

    @Test
    public void givenAudioTracks_withAudioDescribedTrack_whenCheckingContainsAudioDescribedTrack_thenReturnsTrue() {
        AudioTracks audioTracks = AudioTracks.from(Arrays.asList(MAIN_TRACK, AUDIO_DESCRIBED_TRACK));

        boolean containsAudioDescribedTrack = audioTracks.containsPlayerAudioTrackWith(AudioTrackType.AUDIO_DESCRIBED);

        assertThat(containsAudioDescribedTrack).isTrue();
    }

    @Test
    public void givenAudioTracks_withoutAudioDescribedTrack_whenCheckingContainsAudioDescribedTrack_thenReturnsFalse() {
        AudioTracks audioTracks = AudioTracks.from(Collections.singletonList(MAIN_TRACK));

        boolean containsAudioDescribedTrack = audioTracks.containsPlayerAudioTrackWith(AudioTrackType.AUDIO_DESCRIBED);

        assertThat(containsAudioDescribedTrack).isFalse();
    }

    @Test
    public void givenAudioTracks_withAudioDescribedTrack_whenGettingFirstAudioDescribedTrack_thenReturnsAudioDescribedTrack() {
        AudioTracks audioTracks = AudioTracks.from(Arrays.asList(MAIN_TRACK, AUDIO_DESCRIBED_TRACK));

        Optional<PlayerAudioTrack> playerAudioTrack = audioTracks.firstPlayerAudioTrackWith(AudioTrackType.AUDIO_DESCRIBED);

        assertThat(playerAudioTrack.isPresent()).isTrue();
        assertThat(playerAudioTrack.get()).isEqualTo(AUDIO_DESCRIBED_TRACK);
    }

    @Test
    public void givenAudioTracks_withoutAudioDescribedTrack_whenGettingFirstAudioDescribedTrack_thenReturnsAbsent() {
        AudioTracks audioTracks = AudioTracks.from(Collections.singletonList(MAIN_TRACK));

        Optional<PlayerAudioTrack> playerAudioTrack = audioTracks.firstPlayerAudioTrackWith(AudioTrackType.AUDIO_DESCRIBED);

        assertThat(playerAudioTrack.isPresent()).isFalse();
    }

    @Test
    public void givenAudioTracks_whenGettingPlayerAudioTrack_thenReturnsPlayerAudioTrack() {
        AudioTracks audioTracks = AudioTracks.from(Collections.singletonList(MAIN_TRACK));

        PlayerAudioTrack playerAudioTrack = audioTracks.getPlayerAudioTrackAt(FIRST_INDEX);

        assertThat(playerAudioTrack).isEqualTo(MAIN_TRACK);
    }
}
