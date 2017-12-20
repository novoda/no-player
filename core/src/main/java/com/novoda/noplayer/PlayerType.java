package com.novoda.noplayer;

import com.novoda.noplayer.drm.DrmType;

public enum PlayerType {
    MEDIA_PLAYER(new AndroidMediaPlayerCapabilities()),
    EXO_PLAYER(new ExoPlayerCapabilities());

    private final PlayerCapabilities playerCapabilities;

    PlayerType(PlayerCapabilities playerCapabilities) {
        this.playerCapabilities = playerCapabilities;
    }

    boolean supports(DrmType drmType) {
        return playerCapabilities.supports(drmType);
    }

    public static PlayerType from(String rawPlayerType) {
        for (PlayerType playerType : values()) {
            if (playerType.name().equalsIgnoreCase(rawPlayerType)) {
                return playerType;
            }
        }
        throw new UnknownPlayerTypeException(rawPlayerType);
    }

    static class UnknownPlayerTypeException extends RuntimeException {

        UnknownPlayerTypeException(String rawPlayerType) {
            super("Can't create PlayerType from : " + rawPlayerType);
        }
    }

}
