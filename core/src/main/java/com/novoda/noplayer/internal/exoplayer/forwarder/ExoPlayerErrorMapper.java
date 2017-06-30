package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.PlayerErrorType;
import com.novoda.noplayer.drm.StreamingModularDrm;

import java.io.IOException;

final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // static class.
    }

    static Player.PlayerError errorFor(Exception e) {
        if (e instanceof HttpDataSource.InvalidResponseCodeException) {
            return new NoPlayerError(PlayerErrorType.INVALID_RESPONSE_CODE, e);
        }

        if (e instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.MALFORMED_CONTENT, e);
        }

        Throwable cause = e.getCause();
        if (e.getCause() instanceof MediaCodec.CryptoException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_DECRYPTION, e);
        }

        if (cause instanceof StreamingModularDrm.DrmRequestException) {
            return new NoPlayerError(PlayerErrorType.FAILED_DRM_REQUEST, e);
        }

        if (e instanceof IOException || cause instanceof IOException) {
            return new NoPlayerError(PlayerErrorType.CONNECTIVITY_ERROR, cause == null ? e : cause);
        }
        return new NoPlayerError(PlayerErrorType.UNKNOWN, e);
    }
}
