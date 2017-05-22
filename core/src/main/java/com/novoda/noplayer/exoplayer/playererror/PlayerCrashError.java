package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class PlayerCrashError implements Player.PlayerError {

    private final String errorCode;
    private final Throwable cause;

    public PlayerCrashError(String errorCode, Throwable cause) {
        this.errorCode = errorCode;
        this.cause = cause;
    }

    @Override
    public String getType() {
        return errorCode;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
