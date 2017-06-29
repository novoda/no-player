package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.novoda.noplayer.PlayerType;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ExoPlayerInformationTest {

    private ExoPlayerInformation playerInformation;

    @Before
    public void setUp() {
        playerInformation = new ExoPlayerInformation();
    }

    @Test
    public void whenReadingName_thenReturnsExoPlayer() {

        String name = playerInformation.getName();

        assertThat(name).isEqualTo("ExoPlayer");
    }

    @Test
    public void whenReadingVersion_thenReturnsExoPlayerLibraryVersion() {

        String version = playerInformation.getVersion();

        assertThat(version).isEqualTo(ExoPlayerLibraryInfo.VERSION);
    }

    @Test
    public void whenPlayerType_thenReturnsExoPlayer() {

        PlayerType playerType = playerInformation.getPlayerType();

        assertThat(playerType).isEqualTo(PlayerType.EXO_PLAYER);
    }
}
