package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerType;

class ExoPlayerInformation implements PlayerInformation {
    @Override
    public PlayerType getPlayerType() {
        return PlayerType.EXO_PLAYER;
    }

    @Override
    public String getVersion() {
        return ExoPlayerLibraryInfo.VERSION;
    }

    @Override
    public String getName() {
        return "ExoPlayer";
    }
}
