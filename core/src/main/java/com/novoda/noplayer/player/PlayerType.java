package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.notils.exception.DeveloperError;

public enum PlayerType implements PlayerCapabilities {
    MEDIA_PLAYER(new AndroidMediaPlayerCapabilities()),
    EXO_PLAYER(new PlayerCapabilities() {
        @Override
        public boolean supports(DrmHandler drmHandler) {
            return false;
        }
    });


    private final PlayerCapabilities playerCapabilities;

    PlayerType(PlayerCapabilities playerCapabilities) {
        this.playerCapabilities = playerCapabilities;
    }

    @Override
    public boolean supports(DrmHandler drmHandler) {
        return playerCapabilities.supports(drmHandler);
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
