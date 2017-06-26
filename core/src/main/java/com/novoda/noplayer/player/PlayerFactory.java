package com.novoda.noplayer.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImplFactory;
import com.novoda.utils.AndroidDeviceVersion;

public class PlayerFactory {

    private static final boolean USE_SECURE_CODEC = false;

    private final Context context;
    private final PrioritizedPlayerTypes prioritizedPlayerTypes;
    private final NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
    private final NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;
    private final DrmSessionCreatorFactory drmSessionCreatorFactory;

    public PlayerFactory(Context context, PrioritizedPlayerTypes prioritizedPlayerTypes) {
        this(
                context,
                prioritizedPlayerTypes,
                NoPlayerExoPlayerCreator.newInstance(new Handler(Looper.getMainLooper())),
                NoPlayerMediaPlayerCreator.newInstance(),
                new DrmSessionCreatorFactory(
                        AndroidDeviceVersion.newInstance(),
                        new Handler(Looper.getMainLooper())
                )
        );
    }

    PlayerFactory(Context context,
                  PrioritizedPlayerTypes prioritizedPlayerTypes,
                  NoPlayerExoPlayerCreator noPlayerExoPlayerCreator,
                  NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator,
                  DrmSessionCreatorFactory drmSessionCreatorFactory) {
        this.context = context;
        this.prioritizedPlayerTypes = prioritizedPlayerTypes;
        this.noPlayerExoPlayerCreator = noPlayerExoPlayerCreator;
        this.noPlayerMediaPlayerCreator = noPlayerMediaPlayerCreator;
        this.drmSessionCreatorFactory = drmSessionCreatorFactory;
    }

    public Player create() {
        return create(DrmType.NONE, DrmHandler.NO_DRM);
    }

    public Player create(DrmType drmType, DrmHandler drmHandler) {
        return create(drmType, drmHandler, USE_SECURE_CODEC);
    }

    public Player create(DrmType drmType, DrmHandler drmHandler, boolean useSecureCodec) {
        for (PlayerType player : prioritizedPlayerTypes) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, drmType, drmHandler, useSecureCodec);
            }
        }
        throw UnableToCreatePlayerException.unhandledDrmType(drmType);
    }

    private Player createPlayerForType(PlayerType playerType, DrmType drmType, DrmHandler drmHandler, boolean useSecureCodec) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return noPlayerMediaPlayerCreator.createMediaPlayer(context);
            case EXO_PLAYER:
                DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(drmType, drmHandler);
                return noPlayerExoPlayerCreator.createExoPlayer(context, drmSessionCreator, useSecureCodec);
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }

    public static class UnableToCreatePlayerException extends RuntimeException {

        static UnableToCreatePlayerException unhandledDrmType(DrmType drmType) {
            return new UnableToCreatePlayerException("Unhandled DrmType: " + drmType);
        }

        public static UnableToCreatePlayerException noDrmHandlerFor(DrmType drmType) {
            return new UnableToCreatePlayerException("No DrmHandler for DrmType: " + drmType);
        }

        static UnableToCreatePlayerException unhandledPlayerType(PlayerType playerType) {
            return new UnableToCreatePlayerException("Unhandled player type: " + playerType.name());
        }

        public static UnableToCreatePlayerException deviceDoesNotMeetTargetApiException(DrmType drmType,
                                                                                        int targetApiLevel,
                                                                                        AndroidDeviceVersion actualApiLevel) {
            return new UnableToCreatePlayerException(
                    "Device must be target: "
                            + targetApiLevel
                            + " but was: "
                            + actualApiLevel.sdkInt()
                            + " for DRM type: "
                            + drmType.name()
            );
        }

        UnableToCreatePlayerException(String reason) {
            super(reason);
        }
    }

    static class NoPlayerMediaPlayerCreator {

        private final AndroidMediaPlayerImplFactory factory;

        static NoPlayerMediaPlayerCreator newInstance() {
            AndroidMediaPlayerImplFactory factory = new AndroidMediaPlayerImplFactory();
            return new NoPlayerMediaPlayerCreator(factory);
        }

        NoPlayerMediaPlayerCreator(AndroidMediaPlayerImplFactory factory) {
            this.factory = factory;
        }

        Player createMediaPlayer(Context context) {
            AndroidMediaPlayerImpl player = factory.create(context);
            player.initialise();
            return player;
        }
    }
}
