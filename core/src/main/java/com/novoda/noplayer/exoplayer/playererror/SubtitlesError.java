package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class SubtitlesError implements Player.PlayerError {

    private final String type;
    private final Throwable cause;

    public SubtitlesError(String type, Throwable cause) {
        this.type = type;
        this.cause = cause;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
