package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class DrmInitiatingError implements Player.PlayerError {

    private final Throwable cause;

    public DrmInitiatingError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "DrmInitiatingError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
