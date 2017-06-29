package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerErrorType;

import java.io.IOException;

class MediaSourceOnErrorForwarder implements ExtractorMediaSource.EventListener {

    private final Player.ErrorListener errorListener;

    MediaSourceOnErrorForwarder(Player.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public void onLoadError(IOException error) {
        Player.PlayerError playerError = new NoPlayerError(PlayerErrorType.MEDIA_SOURCE_ERROR, error);
        errorListener.onError(playerError);
    }
}
