package com.novoda.noplayer.internal.exoplayer.error;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

public final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // Static class.
    }

    public static NoPlayer.PlayerError errorFor(ExoPlaybackException exception) {
        String message = ErrorFormatter.formatMessage(exception.getCause());

        switch (exception.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                return SourceErrorMapper.map(exception.getSourceException(), message);
            case ExoPlaybackException.TYPE_RENDERER:
                return RendererErrorMapper.map(exception.getRendererException(), message);
            case ExoPlaybackException.TYPE_UNEXPECTED:
                return UnexpectedErrorMapper.map(exception.getUnexpectedException(), message);
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, message);
        }
    }
}
