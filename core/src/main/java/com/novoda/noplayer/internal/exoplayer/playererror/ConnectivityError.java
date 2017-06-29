package com.novoda.noplayer.internal.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class ConnectivityError implements Player.PlayerError {

    private final Throwable cause;

    public ConnectivityError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "ConnectivityError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
