package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class InvalidResponseCodeError implements Player.PlayerError {

    private final Throwable cause;

    public InvalidResponseCodeError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "InvalidResponseCodeError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
