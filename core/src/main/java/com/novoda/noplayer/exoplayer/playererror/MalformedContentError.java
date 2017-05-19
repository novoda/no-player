package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class MalformedContentError implements Player.PlayerError {

    private final Throwable cause;

    public MalformedContentError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "MalformedContentError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
