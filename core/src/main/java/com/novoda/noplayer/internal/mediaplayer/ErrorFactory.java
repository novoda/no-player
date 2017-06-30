package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerErrorType;

public final class ErrorFactory {

    private ErrorFactory() {
        // no instances
    }

    public static Player.PlayerError createErrorFrom(int type, int extra) {
        switch (type) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                return new NoPlayerError(PlayerErrorType.STREAMED_VIDEO_ERROR, new Throwable(String.valueOf(extra)));
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                return new NoPlayerError(PlayerErrorType.MEDIA_FORMAT_NOT_RECOGNISED, new Throwable(String.valueOf(extra)));
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                return new NoPlayerError(PlayerErrorType.MEDIA_SERVER_DIED, new Throwable(String.valueOf(extra)));
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, new Throwable(String.valueOf(extra)));
        }
    }
}
