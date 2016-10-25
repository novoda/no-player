package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.exoplayer.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerFacade;
import com.novoda.noplayer.exoplayer.ExoPlayerImpl;
import com.novoda.noplayer.exoplayer.ProvisioningModularDrmCallback;
import com.novoda.noplayer.exoplayer.RendererFactory;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerFacade;
import com.novoda.noplayer.mediaplayer.AndroidMediaPlayerImpl;
import com.novoda.notils.exception.DeveloperError;

public class PlayerFactory {

    private final Context context;
    private final PrioritisedPlayers prioritisedPlayers;

    public PlayerFactory(Context context, PrioritisedPlayers prioritisedPlayers) {
        this.context = context;
        this.prioritisedPlayers = prioritisedPlayers;
    }

    public Player create(DrmHandler drm) {
        for (PlayerType playerType : prioritisedPlayers) {
            if (playerType.supports(drm)) {
                return createPlayerForType(playerType, drm);
            }
        }
        throw new DeveloperError("No player available to handle : " + drm);
    }

    private Player createPlayerForType(PlayerType playerType, DrmHandler drm) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return createMediaPlayer();
            case EXO_PLAYER:
                return createExoPlayer(createDrmSessionCreatorFor(drm));
            default:
                throw new DeveloperError("Unhandled player type : " + playerType.name());
        }
    }

    private DrmSessionCreator createDrmSessionCreatorFor(DrmHandler drm) {
        if (drm == DrmHandler.NO_DRM) {
            return new NoDrmSessionCreator();
        } else if (drm instanceof StreamingModularDrm) {
            ProvisionExecutor provisionExecutor = ProvisionExecutor.newInstance();
            ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback((StreamingModularDrm) drm, provisionExecutor);
            return new StreamingDrmSessionCreator(mediaDrmCallback);
        } else if (drm instanceof DownloadedModularDrm) {
            return new LocalDrmSessionCreator((DownloadedModularDrm) drm);
        } else {
            throw new DeveloperError("Unhandled DrmHandler : " + drm);
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
}
