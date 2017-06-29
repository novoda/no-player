package com.novoda.noplayer.internal.mediaplayer;

import android.os.Build;

import com.novoda.noplayer.PlayerInformation;
import com.novoda.noplayer.PlayerType;

class MediaPlayerInformation implements PlayerInformation {

    private final InternalMediaPlayerTypeReader mediaPlayerTypeReader;

    MediaPlayerInformation(InternalMediaPlayerTypeReader mediaPlayerTypeReader) {
        this.mediaPlayerTypeReader = mediaPlayerTypeReader;
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
        return "MediaPlayer: " + mediaPlayerTypeReader.getPlayerType().getName();
    }
}
