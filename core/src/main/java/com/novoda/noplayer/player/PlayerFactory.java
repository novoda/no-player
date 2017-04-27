package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.exoplayer.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerFacade;
import com.novoda.noplayer.exoplayer.ExoPlayerImpl;
import com.novoda.noplayer.exoplayer.ProvisioningModularDrmCallback;
import com.novoda.noplayer.exoplayer.RendererFactory;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerFacade;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;

public class PlayerFactory {

    private final Context context;
    private final PrioritizedPlayers prioritizedPlayers;

    public PlayerFactory(Context context, PrioritizedPlayers prioritizedPlayers) {
        this.context = context;
        this.prioritizedPlayers = prioritizedPlayers;
    }

    public Player create() {
        return create(DrmType.NONE, DrmHandler.NO_DRM);
    }

    public Player create(DrmType drmType, DrmHandler drmHandler) {
        for (PlayerType player : prioritizedPlayers) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, drmType, drmHandler);
            }
        }
        throw UnableToCreatePlayerException.unknownDrmType(drmType);
    }

    private Player createPlayerForType(PlayerType playerType, DrmType drmType, DrmHandler drmHandler) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return createMediaPlayer();
            case EXO_PLAYER:
                return createExoPlayer(createDrmSessionCreatorFor(drmType, drmHandler));
            default:
                throw UnableToCreatePlayerException.unknownPlayerType(playerType);
        }
    }

    private DrmSessionCreator createDrmSessionCreatorFor(DrmType drmType, DrmHandler drmHandler) {
        switch (drmType) {
            case NONE:
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
                ProvisionExecutor provisionExecutor = ProvisionExecutor.newInstance();
                ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback((StreamingModularDrm) drmHandler, provisionExecutor);
                return new StreamingDrmSessionCreator(mediaDrmCallback);
            case WIDEVINE_MODULAR_DOWNLOAD:
                return new LocalDrmSessionCreator((DownloadedModularDrm) drmHandler);
            default:
                throw UnableToCreatePlayerException.noDrmHandlerFor(drmType);
        }
    }

    private Player createExoPlayer(DrmSessionCreator drmSessionCreator) {
        RendererFactory rendererFactory = createRendererFactory(drmSessionCreator);
        ExoPlayerFacade exoPlayerFacade = new ExoPlayerFacade(rendererFactory);
        return new ExoPlayerImpl(exoPlayerFacade);
    }

    private RendererFactory createRendererFactory(DrmSessionCreator drmSessionCreator) {
        return new RendererFactory(context, drmSessionCreator);
    }

    private Player createMediaPlayer() {
        AndroidMediaPlayerFacade androidMediaPlayer = new AndroidMediaPlayerFacade(context);
        return new AndroidMediaPlayerImpl(androidMediaPlayer);
    }

    static class UnableToCreatePlayerException extends RuntimeException {

        static UnableToCreatePlayerException unknownDrmType(DrmType drmType) {
            return new UnableToCreatePlayerException("Unhandled DrmType: " + drmType);
        }

        static UnableToCreatePlayerException noDrmHandlerFor(DrmType drmType) {
            return new UnableToCreatePlayerException("No DrmHandler for DrmType: " + drmType);
        }

        static UnableToCreatePlayerException unknownPlayerType(PlayerType playerType) {
            return new UnableToCreatePlayerException("Unhandled player type: " + playerType.name());
        }

        UnableToCreatePlayerException(String reason) {
            super(reason);
        }

    }

}
