package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.NoDrmSessionCreator;
import com.novoda.noplayer.drm.ProvisioningModularDrmCallback;
import com.novoda.noplayer.drm.StreamingDrmSessionCreator;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.exoplayer.ExoPlayerTwoImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImplFactory;

public class PlayerFactory {

    private final Context context;
    private final PrioritizedPlayerTypes prioritizedPlayerTypes;
    private final ExoPlayerCreator exoPlayerCreator;
    private final MediaPlayerCreator mediaPlayerCreator;

    public PlayerFactory(Context context, PrioritizedPlayerTypes prioritizedPlayerTypes) {
        this(context, prioritizedPlayerTypes, new ExoPlayerCreator(), new MediaPlayerCreator());
    }

    PlayerFactory(Context context, PrioritizedPlayerTypes prioritizedPlayerTypes, ExoPlayerCreator exoPlayerCreator, MediaPlayerCreator mediaPlayerCreator) {
        this.context = context;
        this.prioritizedPlayerTypes = prioritizedPlayerTypes;
        this.exoPlayerCreator = exoPlayerCreator;
        this.mediaPlayerCreator = mediaPlayerCreator;
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
                DrmSessionCreator drmSessionCreator = createDrmSessionCreatorFor(drmType, drmHandler);
                return exoPlayerCreator.createExoPlayer(context, drmSessionCreator);
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }

    private DrmSessionCreator createDrmSessionCreatorFor(DrmType drmType, DrmHandler drmHandler) {
        switch (drmType) {
            case NONE:
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
                ProvisionExecutor provisionExecutor = ProvisionExecutor.newInstance();
                ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                        (StreamingModularDrm) drmHandler,
                        provisionExecutor
                );
                return new StreamingDrmSessionCreator(mediaDrmCallback);
            case WIDEVINE_MODULAR_DOWNLOAD:
                // TODO
                // return new LocalDrmSessionCreator((DownloadedModularDrm) drmHandler);
                return new NoDrmSessionCreator();
            default:
                throw UnableToCreatePlayerException.noDrmHandlerFor(drmType);
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

        Player createExoPlayer(Context context, DrmSessionCreator drmSessionCreator) {
            return ExoPlayerTwoImpl.newInstance(context, drmSessionCreator);
        }
    }

    static class MediaPlayerCreator {

        Player createMediaPlayer(Context context) {
            AndroidMediaPlayerImpl player = new AndroidMediaPlayerImplFactory().create(context);
            player.initialise();
            return player;
        }
    }
}
