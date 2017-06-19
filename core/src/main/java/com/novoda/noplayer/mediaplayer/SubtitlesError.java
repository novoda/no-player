package com.novoda.noplayer.mediaplayer;

import com.novoda.noplayer.Player;

class SubtitlesError implements Player.PlayerError {

    private final String type;
    private final Throwable cause;

    SubtitlesError(String type, Throwable cause) {
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
