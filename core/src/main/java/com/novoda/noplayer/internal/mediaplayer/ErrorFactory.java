package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

import static com.novoda.noplayer.internal.mediaplayer.ErrorFormatter.formatMessage;

public final class ErrorFactory {

    private ErrorFactory() {
        // no instances
    }

    public static NoPlayer.PlayerError createErrorFrom(int type, int extra) {
        switch (type) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                return new NoPlayerError(PlayerErrorType.STREAMED_VIDEO_ERROR, formatMessage(type, extra));
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                return new NoPlayerError(PlayerErrorType.MEDIA_FORMAT_NOT_RECOGNIZED, formatMessage(type, extra));
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                return new NoPlayerError(PlayerErrorType.MEDIA_SERVER_DIED, formatMessage(type, extra));
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, formatMessage(type, extra));
        }
    }
}
