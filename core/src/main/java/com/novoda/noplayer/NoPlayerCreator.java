package com.novoda.noplayer;

import android.content.Context;

import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorException;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSessionCreatorFactory;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;

import java.util.List;

class NoPlayerCreator {

    private final Context context;
    private final List<PlayerType> prioritizedPlayerTypes;
    private final NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
    private final NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;
    private final DrmSessionCreatorFactory drmSessionCreatorFactory;

    NoPlayerCreator(Context context,
                    List<PlayerType> prioritizedPlayerTypes,
                    NoPlayerExoPlayerCreator noPlayerExoPlayerCreator,
                    NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator,
                    DrmSessionCreatorFactory drmSessionCreatorFactory) {
        this.context = context;
        this.prioritizedPlayerTypes = prioritizedPlayerTypes;
        this.noPlayerExoPlayerCreator = noPlayerExoPlayerCreator;
        this.noPlayerMediaPlayerCreator = noPlayerMediaPlayerCreator;
        this.drmSessionCreatorFactory = drmSessionCreatorFactory;
    }

    NoPlayer create(DrmType drmType, DrmHandler drmHandler, boolean downgradeSecureDecoder, boolean allowCrossProtocolRedirects) {
        for (PlayerType player : prioritizedPlayerTypes) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, drmType, drmHandler, downgradeSecureDecoder, allowCrossProtocolRedirects);
            }
        }
        throw UnableToCreatePlayerException.unhandledDrmType(drmType);
    }

    private NoPlayer createPlayerForType(PlayerType playerType,
                                         DrmType drmType,
                                         DrmHandler drmHandler,
                                         boolean downgradeSecureDecoder,
                                         boolean allowCrossProtocolRedirects) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return noPlayerMediaPlayerCreator.createMediaPlayer(context);
            case EXO_PLAYER:
                try {
                    DrmSessionCreator drmSessionCreator = drmSessionCreatorFactory.createFor(drmType, drmHandler);
                    return noPlayerExoPlayerCreator.createExoPlayer(
                            context,
                            drmSessionCreator,
                            downgradeSecureDecoder,
                            allowCrossProtocolRedirects
                    );
                } catch (DrmSessionCreatorException exception) {
                    throw new UnableToCreatePlayerException(exception);
                }
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }
}
