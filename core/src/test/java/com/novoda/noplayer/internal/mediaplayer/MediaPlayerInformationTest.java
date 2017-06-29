package com.novoda.noplayer.internal.mediaplayer;

import android.os.Build;

import com.novoda.noplayer.PlayerType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MediaPlayerInformationTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private MediaPlayerTypeReader playerTypeReader;

    private MediaPlayerInformation playerInformation;

    @Before
    public void setUp() {
        playerInformation = new MediaPlayerInformation(playerTypeReader);
    }

    @Test
    public void givenInternalNuPlayer_whenReadingName_thenReturnsMediaPlayerNuPlayer() {
        given(playerTypeReader.getPlayerType()).willReturn(AndroidMediaPlayerType.NU);

        String name = playerInformation.getName();

        assertThat(name).isEqualTo("MediaPlayer: NuPlayer");
    }

    @Test
    public void whenReadingVersion_thenReturnsAndroidBuildVersion() {

        String version = playerInformation.getVersion();

        assertThat(version).isEqualTo(Build.VERSION.RELEASE);
    }

    @Test
    public void whenPlayerType_thenReturnsMediaPlayer() {

        PlayerType playerType = playerInformation.getPlayerType();

        assertThat(playerType).isEqualTo(PlayerType.MEDIA_PLAYER);
    }
}
