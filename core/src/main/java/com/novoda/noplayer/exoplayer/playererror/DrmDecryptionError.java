package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class DrmDecryptionError implements Player.PlayerError {

    private final Throwable cause;

    public DrmDecryptionError(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getType() {
        return "DrmDecryptionError";
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
