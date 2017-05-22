package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class DrmRequestError implements Player.PlayerError {

    private final Throwable cause;

    public DrmRequestError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "DrmRequestError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
