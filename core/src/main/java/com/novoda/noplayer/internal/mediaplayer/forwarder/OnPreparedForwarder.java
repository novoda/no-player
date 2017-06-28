package com.novoda.noplayer.internal.mediaplayer.forwarder;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerState;

class OnPreparedForwarder implements MediaPlayer.OnPreparedListener {

    private final Player.PreparedListener preparedListener;
    private final PlayerState playerState;

    OnPreparedForwarder(Player.PreparedListener preparedListener, PlayerState playerState) {
        this.preparedListener = preparedListener;
        this.playerState = playerState;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        preparedListener.onPrepared(playerState);
    }
}
