package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImpl;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImplFactory;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImplFactory;

public class PlayerFactory {

    private final Context context;
    private final PrioritizedPlayerTypes prioritizedPlayerTypes;
    private final ExoPlayerCreator exoPlayerCreator;
    private final MediaPlayerCreator mediaPlayerCreator;
    private final DrmSessionCreatorFactory drmSessionCreatorFactory;

    public PlayerFactory(Context context, PrioritizedPlayerTypes prioritizedPlayerTypes) {
        this(context, prioritizedPlayerTypes, ExoPlayerCreator.newInstance(), MediaPlayerCreator.newInstance(), new DrmSessionCreatorFactory());
    }

    PlayerFactory(Context context,
                  PrioritizedPlayerTypes prioritizedPlayerTypes,
                  ExoPlayerCreator exoPlayerCreator,
                  MediaPlayerCreator mediaPlayerCreator,
                  DrmSessionCreatorFactory drmSessionCreatorFactory) {
        this.context = context;
        this.prioritizedPlayerTypes = prioritizedPlayerTypes;
        this.exoPlayerCreator = exoPlayerCreator;
        this.mediaPlayerCreator = mediaPlayerCreator;
        this.drmSessionCreatorFactory = drmSessionCreatorFactory;
    }

    public Player create() {
        return create(DrmType.NONE, DrmHandler.NO_DRM);
    }

    public Player create(DrmType drmType, DrmHandler drmHandler) {
        for (PlayerType player : prioritizedPlayerTypes) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, drmType, drmHandler);
            }
        }
        throw UnableToCreatePlayerException.unhandledDrmType(drmType);
    }

    private Player createPlayerForType(PlayerType playerType, DrmType drmType, DrmHandler drmHandler) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return mediaPlayerCreator.createMediaPlayer(context);
            case EXO_PLAYER:
                DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(drmType, drmHandler);
                return exoPlayerCreator.createExoPlayer(context, drmSessionCreator);
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }

    static class UnableToCreatePlayerException extends RuntimeException {

        static UnableToCreatePlayerException unhandledDrmType(DrmType drmType) {
            return new UnableToCreatePlayerException("Unhandled DrmType: " + drmType);
        }

        static UnableToCreatePlayerException noDrmHandlerFor(DrmType drmType) {
            return new UnableToCreatePlayerException("No DrmHandler for DrmType: " + drmType);
        }

        static UnableToCreatePlayerException unhandledPlayerType(PlayerType playerType) {
            return new UnableToCreatePlayerException("Unhandled player type: " + playerType.name());
        }

        UnableToCreatePlayerException(String reason) {
            super(reason);
        }
    }

    static class ExoPlayerCreator {

        private final ExoPlayerTwoImplFactory factory;

        static ExoPlayerCreator newInstance() {
            ExoPlayerTwoImplFactory factory = new ExoPlayerTwoImplFactory();
            return new ExoPlayerCreator(factory);
        }

        ExoPlayerCreator(ExoPlayerTwoImplFactory factory) {
            this.factory = factory;
        }

        Player createExoPlayer(Context context, DrmSessionCreator drmSessionCreator) {
            ExoPlayerTwoImpl player = factory.create(context, drmSessionCreator);
            player.initialise();
            return player;
        }
    }

    static class MediaPlayerCreator {

        private final AndroidMediaPlayerImplFactory factory;

        static MediaPlayerCreator newInstance() {
            AndroidMediaPlayerImplFactory factory = new AndroidMediaPlayerImplFactory();
            return new MediaPlayerCreator(factory);
        }

        MediaPlayerCreator(AndroidMediaPlayerImplFactory factory) {
            this.factory = factory;
        }

        Player createMediaPlayer(Context context) {
            AndroidMediaPlayerImpl player = factory.create(context);
            player.initialise();
            return player;
        }
    }
}
