package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.internal.exoplayer.mediasource.AudioTrackType;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import utils.ExceptionMatcher;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static utils.ExceptionMatcher.matches;

public class AndroidMediaPlayerAudioTrackSelectorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final int NO_FORMAT = 0;
    private static final int NO_CHANNELS = -1;
    private static final int NO_FREQUENCY = -1;
    private static final int AUDIO_TRACK_INDEX = 2;

    private static final String NO_MIME_TYPE = "";
    private static final String ANY_LANGUAGE = "english";

    private static final NoPlayerTrackInfo AUDIO_TRACK_INFO = mock(NoPlayerTrackInfo.class);
    private static final NoPlayerTrackInfo VIDEO_TRACK_INFO = mock(NoPlayerTrackInfo.class);
    private static final NoPlayerTrackInfo UNKNOWN_TRACK_INFO = mock(NoPlayerTrackInfo.class);

    @Mock
    private TrackInfosFactory trackInfosFactory;
    @Mock
    private MediaPlayer mediaPlayer;
    @Mock
    private PlayerAudioTrack playerAudioTrack;

    private AndroidMediaPlayerAudioTrackSelector trackSelector;

    @Before
    public void setUp() {
        trackSelector = new AndroidMediaPlayerAudioTrackSelector(trackInfosFactory);
    }

    @Test
    public void givenNullMediaPlayer_whenGettingAudioTracks_thenThrowsIllegalState() {
        thrown.expect(ExceptionMatcher.matches("You can only call getAudioTracks() when video is prepared.", IllegalStateException.class));

        trackSelector.getAudioTracks(null);
    }

    @Test
    public void givenTrackSelectorContainsUnsupportedTracks_whenGettingAudioTracks_thenReturnsOnlySupportedTracks() {
        givenTrackSelectorContainsUnsupportedTracks();

        AudioTracks audioTracks = trackSelector.getAudioTracks(mediaPlayer);

        assertThat(audioTracks).isEqualTo(expectedAudioTrack());
    }

    @Test
    public void givenNullMediaPlayer_whenSelectingAudioTrack_thenThrowsIllegalState() {
        thrown.expect(matches("You can only call selectAudioTrack() when video is prepared.", IllegalStateException.class));

        trackSelector.selectAudioTrack(null, mock(PlayerAudioTrack.class));
    }

    @Test
    public void whenSelectingAudioTrack_thenMediaPlayerSelectsAudioTrack() {
        PlayerAudioTrack playerAudioTrack = mock(PlayerAudioTrack.class);
        given(playerAudioTrack.groupIndex()).willReturn(AUDIO_TRACK_INDEX);

        trackSelector.selectAudioTrack(mediaPlayer, playerAudioTrack);

        verify(mediaPlayer).selectTrack(AUDIO_TRACK_INDEX);
    }

    private void givenTrackSelectorContainsUnsupportedTracks() {
        given(AUDIO_TRACK_INFO.type()).willReturn(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO);
        given(AUDIO_TRACK_INFO.language()).willReturn(ANY_LANGUAGE);
        given(VIDEO_TRACK_INFO.type()).willReturn(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_VIDEO);
        given(VIDEO_TRACK_INFO.language()).willReturn(ANY_LANGUAGE);
        given(UNKNOWN_TRACK_INFO.type()).willReturn(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_UNKNOWN);
        given(UNKNOWN_TRACK_INFO.language()).willReturn(ANY_LANGUAGE);

        NoPlayerTrackInfos noPlayerTrackInfos = new NoPlayerTrackInfos(
                Arrays.asList(
                        VIDEO_TRACK_INFO,
                        UNKNOWN_TRACK_INFO,
                        AUDIO_TRACK_INFO
                )
        );
        given(trackInfosFactory.createFrom(mediaPlayer)).willReturn(noPlayerTrackInfos);
    }

    private AudioTracks expectedAudioTrack() {
        return AudioTracks.from(
                Collections.singletonList(
                        new PlayerAudioTrack(
                                AUDIO_TRACK_INDEX,
                                NO_FORMAT,
                                String.valueOf(AUDIO_TRACK_INFO.hashCode()),
                                AUDIO_TRACK_INFO.language(),
                                NO_MIME_TYPE,
                                NO_CHANNELS,
                                NO_FREQUENCY,
                                AudioTrackType.MAIN
                        )
                )
        );
    }
}
