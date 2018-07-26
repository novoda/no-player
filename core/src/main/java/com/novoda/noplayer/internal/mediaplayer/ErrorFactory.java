package com.novoda.noplayer.internal.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

public final class ErrorFactory {

    private ErrorFactory() {
        // no instances
    }

    @SuppressWarnings({"PMD.StdCyclomaticComplexity", "PMD.CyclomaticComplexity"})
    public static NoPlayer.PlayerError createErrorFrom(int type, int extra) {
        String message = String.valueOf(extra);
        switch (type) {
            case MediaPlayer.MEDIA_ERROR_IO:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_IO, message);
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_MALFORMED, message);
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK, message);
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_INFO_NOT_SEEKABLE, message);
            case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_SUBTITLE_TIMED_OUT, message);
            case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                return new NoPlayerError(PlayerErrorType.SOURCE, DetailErrorType.MEDIA_PLAYER_UNSUPPORTED_SUBTITLE, message);

            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.MEDIA_PLAYER_SERVER_DIED, message);
            case MediaPlayer.PREPARE_DRM_STATUS_PREPARATION_ERROR:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.MEDIA_PLAYER_PREPARE_DRM_STATUS_PREPARATION_ERROR, message);
            case MediaPlayer.PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.MEDIA_PLAYER_PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR, message);
            case MediaPlayer.PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.MEDIA_PLAYER_PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR, message);

            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                return new NoPlayerError(PlayerErrorType.CONNECTIVITY, DetailErrorType.MEDIA_PLAYER_TIMED_OUT, message);

            case MediaPlayer.MEDIA_INFO_AUDIO_NOT_PLAYING:
                return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.MEDIA_PLAYER_INFO_AUDIO_NOT_PLAYING, message);
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.MEDIA_PLAYER_BAD_INTERLEAVING, message);
            case MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING:
                return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.MEDIA_PLAYER_INFO_VIDEO_NOT_PLAYING, message);
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.MEDIA_PLAYER_INFO_VIDEO_TRACK_LAGGING, message);
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.MEDIA_PLAYER_UNKNOWN, message);

        }
    }
}
