package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.novoda.noplayer.player.PlayerInformation;
import com.novoda.noplayer.player.PlayerType;

class ExoPlayerInformation implements PlayerInformation {
    @Override
    public PlayerType getPlayerType() {
        return PlayerType.EXO_PLAYER;
    }

    @Override
    public String getVersion() {
        return ExoPlayerLibraryInfo.VERSION;
    }
}
