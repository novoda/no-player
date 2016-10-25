package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import com.novoda.noplayer.Player;

public final class ErrorFactory {

    private ErrorFactory() {
        // no instances
    }

    public static Player.PlayerError createErrorFrom(int type, int extra) {
        switch (type) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                return new StreamedVideoError(type, extra);
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                return new MediaFormatNotRecognizedError(type, extra);
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                return new MediaServerDiedError(type, extra);
            default:
                return new UnknownMediaPlayerError(type, extra);
        }
    }

    private abstract static class MediaPlayerError implements Player.PlayerError {

        private final int type;
        private final int extra;

        protected MediaPlayerError(int type, int extra) {
            this.type = type;
            this.extra = extra;
        }

        @Override
        public String getType() {
            return String.valueOf(type);
        }

        @Override
        public Throwable getCause() {
            return new MediaPlayerErrorThrowable(extra);
        }

    }

    private static class MediaPlayerErrorThrowable extends Throwable {

        MediaPlayerErrorThrowable(int playerErrorExtra) {
            super(String.valueOf(playerErrorExtra));
        }
    }

    public static class StreamedVideoError extends MediaPlayerError {

        protected StreamedVideoError(int type, int extra) {
            super(type, extra);
        }

    }

    public static class MediaFormatNotRecognizedError extends MediaPlayerError {

        protected MediaFormatNotRecognizedError(int type, int extra) {
            super(type, extra);
        }

    }

    public static class MediaServerDiedError extends MediaPlayerError {

        protected MediaServerDiedError(int type, int extra) {
            super(type, extra);
        }

    }

    public static class UnknownMediaPlayerError extends MediaPlayerError {

        protected UnknownMediaPlayerError(int type, int extra) {
            super(type, extra);
        }

    }
}
