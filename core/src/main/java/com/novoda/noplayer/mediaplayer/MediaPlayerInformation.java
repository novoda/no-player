package com.novoda.noplayer.mediaplayer;

import android.os.Build;

import com.novoda.noplayer.player.PlayerInformation;
import com.novoda.noplayer.player.PlayerType;

public class MediaPlayerInformation implements PlayerInformation {

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.MEDIA_PLAYER;
    }

    @Override
    public String getVersion() {
        return Build.VERSION.RELEASE;
    }

}
