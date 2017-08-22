package com.novoda.noplayer;

public class NoPlayerError implements NoPlayer.PlayerError {

    private final PlayerErrorType playerErrorType;
    private final String message;

    public NoPlayerError(PlayerErrorType playerErrorType, String message) {
        this.playerErrorType = playerErrorType;
        this.message = message;
    }

    @Override
    public PlayerErrorType type() {
        return playerErrorType;
    }

    @Override
    public String message() {
        return message;
    }
}
