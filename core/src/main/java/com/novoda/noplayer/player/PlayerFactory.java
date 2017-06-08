package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImplCreator;

public class PlayerFactory {

    private final Context context;
    private final PrioritisedPlayers prioritisedPlayers;

    public PlayerFactory(Context context, PrioritisedPlayers prioritisedPlayers) {
        this.context = context;
        this.prioritisedPlayers = prioritisedPlayers;
    }

    public Player create() {
        return create(DrmHandler.NO_DRM);
    }

    public Player create(DrmHandler drm) {
        for (PlayerType playerType : prioritisedPlayers) {
            if (playerType.supports(drm)) {
                return createPlayerForType(playerType, drm);
            }
        }
        throw UnableToCreatePlayerException.contentNotSupported();
    }

    private Player createPlayerForType(PlayerType playerType, DrmHandler drm) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return createMediaPlayer();
            case EXO_PLAYER:
                return ExoPlayerTwoImpl.newInstance(context);
            default:
                throw UnableToCreatePlayerException.unknownPlayerType(playerType);
        }
    }

    private Player createMediaPlayer() {
        return new AndroidMediaPlayerImplCreator().create(context);
    }

    private static class UnableToCreatePlayerException extends RuntimeException {

        static UnableToCreatePlayerException contentNotSupported() {
            return new UnableToCreatePlayerException("No player available to handle content");
        }

        static UnableToCreatePlayerException unknownDrmHandler(DrmHandler drmHandler) {
            return new UnableToCreatePlayerException("Unhandled DrmHandler : " + drmHandler.getClass().getName());
        }

        static UnableToCreatePlayerException unknownPlayerType(PlayerType playerType) {
            return new UnableToCreatePlayerException("Unhandled player type : " + playerType.name());
        }

        UnableToCreatePlayerException(String reason) {
            super(reason);
        }

    }
}
