package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

import java.io.IOException;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatter.formatMessage;

class MediaSourceOnErrorForwarder implements ExtractorMediaSource.EventListener {

    private final NoPlayer.ErrorListener errorListener;

    MediaSourceOnErrorForwarder(NoPlayer.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public void onLoadError(IOException error) {
        NoPlayer.PlayerError playerError = new NoPlayerError(PlayerErrorType.MEDIA_SOURCE_ERROR, formatMessage(error));
        errorListener.onError(playerError);
    }
}
