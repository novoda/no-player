package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.internal.exoplayer.playererror.MediaSourceError;

import java.io.IOException;

class MediaSourceOnErrorForwarder implements ExtractorMediaSource.EventListener {

    private final Player player;
    private final Player.ErrorListener errorListener;

    MediaSourceOnErrorForwarder(Player player, Player.ErrorListener errorListener) {
        this.player = player;
        this.errorListener = errorListener;
    }

    @Override
    public void onLoadError(IOException error) {
        Player.PlayerError playerError = new MediaSourceError(error);
        errorListener.onError(player, playerError);
    }
}
