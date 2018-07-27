package com.novoda.noplayer;

public class NoPlayerError implements NoPlayer.PlayerError {

    private final PlayerErrorType playerErrorType;
    private final DetailErrorType detailErrorType;
    private final String message;

    public NoPlayerError(PlayerErrorType playerErrorType, DetailErrorType detailErrorType, String message) {
        this.playerErrorType = playerErrorType;
        this.detailErrorType = detailErrorType;
        this.message = message;
    }

    @Override
    public PlayerErrorType type() {
        return playerErrorType;
    }

    @Override
    public DetailErrorType detailType() {
        return detailErrorType;
    }

    @Override
    public String message() {
        return message;
    }
}
