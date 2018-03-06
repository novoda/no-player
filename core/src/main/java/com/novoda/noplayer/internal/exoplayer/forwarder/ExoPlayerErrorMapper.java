package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;
import com.novoda.noplayer.drm.StreamingModularDrm;

import java.io.IOException;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatter.formatMessage;

final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // Static class.
    }

    static NoPlayer.PlayerError errorFor(ExoPlaybackException exception) {
        if (exception.type == ExoPlaybackException.TYPE_SOURCE) {
            IOException sourceException = exception.getSourceException();
            return mapSourceError(sourceException);
        } else if (exception.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception rendererException = exception.getRendererException();
            if (rendererException instanceof DrmSession.DrmSessionException) {
                DrmSession.DrmSessionException drmSessionException = (DrmSession.DrmSessionException) rendererException;
                return mapRendererError(drmSessionException.getCause(), PlayerErrorType.UNKNOWN_DRM_ERROR);
            } else {
                return mapRendererError(rendererException, PlayerErrorType.UNKNOWN_RENDERER_ERROR);
            }
        }
        return new NoPlayerError(PlayerErrorType.UNKNOWN, formatMessage(exception.getCause()));
    }

    private static NoPlayer.PlayerError mapSourceError(IOException sourceException) {
        if (sourceException instanceof HttpDataSource.InvalidResponseCodeException) {
            return new NoPlayerError(PlayerErrorType.INVALID_RESPONSE_CODE, formatMessage(sourceException));
        } else if (sourceException instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.MALFORMED_CONTENT, formatMessage(sourceException));
        } else {
            return new NoPlayerError(PlayerErrorType.CONNECTIVITY_ERROR, formatMessage(sourceException));
        }
    }

    private static NoPlayer.PlayerError mapRendererError(Throwable throwable, PlayerErrorType fallbackErrorType) {
        if (throwable instanceof MediaCodec.CryptoException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_DECRYPTION, formatMessage(throwable));
        } else if (throwable instanceof StreamingModularDrm.DrmRequestException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_REQUEST, formatMessage(throwable));
        } else {
            return new NoPlayerError(fallbackErrorType, formatMessage(throwable));
        }
    }
}
