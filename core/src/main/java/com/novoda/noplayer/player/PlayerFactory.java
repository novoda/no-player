package com.novoda.noplayer.player;

import android.content.Context;

import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.mediaplayer.NoPlayerMediaPlayerCreator;

class PlayerFactory {

    private final Context context;
    private final PrioritizedPlayerTypes prioritizedPlayerTypes;
    private final NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
    private final NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;
    private final DrmSessionCreatorFactory drmSessionCreatorFactory;

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

    Player create(DrmType drmType, DrmHandler drmHandler, boolean downgradeSecureDecoder) {
        for (PlayerType player : prioritizedPlayerTypes) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, drmType, drmHandler, downgradeSecureDecoder);
            }
        }
        throw UnableToCreatePlayerException.unhandledDrmType(drmType);
    }

    private Player createPlayerForType(PlayerType playerType, DrmType drmType, DrmHandler drmHandler, boolean downgradeSecureDecoder) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return noPlayerMediaPlayerCreator.createMediaPlayer(context);
            case EXO_PLAYER:
                DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(drmType, drmHandler);
                return noPlayerExoPlayerCreator.createExoPlayer(context, drmSessionCreator, downgradeSecureDecoder);
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }
}
