package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ParserException;
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

    static NoPlayer.PlayerError errorFor(Exception e) {
        if (e instanceof HttpDataSource.InvalidResponseCodeException) {
            return new NoPlayerError(PlayerErrorType.INVALID_RESPONSE_CODE, formatMessage(e));
        }

        if (e instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.MALFORMED_CONTENT, formatMessage(e));
        }

        Throwable cause = e.getCause();
        if (e.getCause() instanceof MediaCodec.CryptoException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_DECRYPTION, formatMessage(e));
        }

        if (cause instanceof StreamingModularDrm.DrmRequestException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_REQUEST, formatMessage(e));
        }

        if (e instanceof IOException || cause instanceof IOException) {
            return new NoPlayerError(PlayerErrorType.CONNECTIVITY_ERROR, cause == null ? formatMessage(e) : formatMessage(cause));
        }
        return new NoPlayerError(PlayerErrorType.UNKNOWN, formatMessage(e));
    }
}
