package com.novoda.noplayer.internal.mediaplayer;

import android.os.Build;

import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerType;

class MediaPlayerInformation implements PlayerInformation {

    private final PlayerChecker playerChecker;

    MediaPlayerInformation(PlayerChecker playerChecker) {
        this.playerChecker = playerChecker;
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.MEDIA_PLAYER;
    }

    @Override
    public String getVersion() {
        return Build.VERSION.RELEASE;
    }

    @Override
    public String getName() {
        return "MediaPlayer: " + playerChecker.getPlayerType().getName();
    }
}
