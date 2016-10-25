package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class UnknownError implements Player.PlayerError {

    private final Throwable cause;

    public UnknownError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "UnknownError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
