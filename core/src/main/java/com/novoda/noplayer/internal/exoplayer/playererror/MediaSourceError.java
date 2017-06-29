package com.novoda.noplayer.internal.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class MediaSourceError implements Player.PlayerError {

    private final Throwable cause;

    public MediaSourceError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "MediaSourceError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
