package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmType;
import com.novoda.notils.exception.DeveloperError;

public enum PlayerType implements PlayerCapabilities {
    MEDIA_PLAYER(new AndroidMediaPlayerCapabilities()),
    EXO_PLAYER(new ExoPlayerCapabilities());


    private final PlayerCapabilities playerCapabilities;

    PlayerType(PlayerCapabilities playerCapabilities) {
        this.playerCapabilities = playerCapabilities;
    }

    @Override
    public boolean supports(DrmType drmType) {
        return playerCapabilities.supports(drmType);
    }

    public static PlayerType from(String rawPlayerType) {
        for (PlayerType playerType : values()) {
            if (playerType.name().equalsIgnoreCase(rawPlayerType)) {
                return playerType;
            }
        }
        throw new DeveloperError("Can't create PlayerType from : " + rawPlayerType);
    }

}
