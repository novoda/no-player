package com.novoda.noplayer;

public class NoPlayerError implements Player.PlayerError {

    private final PlayerErrorType playerErrorType;
    private final Throwable cause;

    public NoPlayerError(PlayerErrorType playerErrorType, Throwable cause) {
        this.playerErrorType = playerErrorType;
        this.cause = cause;
    }

    @Override
    public String getType() {
        return playerErrorType.name(); // TODO: return the type.
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
