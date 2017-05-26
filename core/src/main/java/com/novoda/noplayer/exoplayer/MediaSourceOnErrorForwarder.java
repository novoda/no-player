package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.playererror.MediaSourceError;
import com.novoda.noplayer.listeners.ErrorListeners;

import java.io.IOException;

class MediaSourceOnErrorForwarder implements ExtractorMediaSource.EventListener {

    private final Player player;
    private final ErrorListeners errorListeners;

    MediaSourceOnErrorForwarder(Player player, ErrorListeners errorListeners) {
        this.player = player;
        this.errorListeners = errorListeners;
    }

    @Override
    public void onLoadError(IOException error) {
        Player.PlayerError playerError = new MediaSourceError(error);
        errorListeners.onError(player, playerError);
    }
}
