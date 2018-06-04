package com.novoda.noplayer;

import android.content.Context;

import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.internal.exoplayer.NoPlayerExoPlayerCreator;
import com.novoda.noplayer.internal.mediaplayer.NoPlayerMediaPlayerCreator;

import java.util.List;

class NoPlayerCreator {

    private final Context context;
    private final List<PlayerType> prioritizedPlayerTypes;
    private final NoPlayerExoPlayerCreator noPlayerExoPlayerCreator;
    private final NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator;

    NoPlayerCreator(Context context,
                    List<PlayerType> prioritizedPlayerTypes,
                    NoPlayerExoPlayerCreator noPlayerExoPlayerCreator,
                    NoPlayerMediaPlayerCreator noPlayerMediaPlayerCreator) {
        this.context = context;
        this.prioritizedPlayerTypes = prioritizedPlayerTypes;
        this.noPlayerExoPlayerCreator = noPlayerExoPlayerCreator;
        this.noPlayerMediaPlayerCreator = noPlayerMediaPlayerCreator;
    }

    NoPlayer create(DrmType drmType, boolean downgradeSecureDecoder) {
        for (PlayerType player : prioritizedPlayerTypes) {
            if (player.supports(drmType)) {
                return createPlayerForType(player, downgradeSecureDecoder);
            }
        }
        throw UnableToCreatePlayerException.unhandledDrmType(drmType);
    }

    private NoPlayer createPlayerForType(PlayerType playerType, boolean downgradeSecureDecoder) {
        switch (playerType) {
            case MEDIA_PLAYER:
                return noPlayerMediaPlayerCreator.createMediaPlayer(context);
            case EXO_PLAYER:
                return noPlayerExoPlayerCreator.createExoPlayer(context, downgradeSecureDecoder);
            default:
                throw UnableToCreatePlayerException.unhandledPlayerType(playerType);
        }
    }
}
