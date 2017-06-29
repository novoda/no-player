package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.exoplayer.playererror.ConnectivityError;
import com.novoda.noplayer.internal.exoplayer.playererror.DrmDecryptionError;
import com.novoda.noplayer.internal.exoplayer.playererror.DrmRequestError;
import com.novoda.noplayer.internal.exoplayer.playererror.InvalidResponseCodeError;
import com.novoda.noplayer.internal.exoplayer.playererror.MalformedContentError;
import com.novoda.noplayer.internal.exoplayer.playererror.UnknownError;

import java.io.IOException;

final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // static class.
    }

    static Player.PlayerError errorFor(Exception e) {
        if (e instanceof HttpDataSource.InvalidResponseCodeException) {
            return new InvalidResponseCodeError(e);
        }
        if (e instanceof ParserException) {
            return new MalformedContentError(e);
        }

        Throwable cause = e.getCause();
        if (e.getCause() instanceof MediaCodec.CryptoException) {
            return new DrmDecryptionError(e);
        }
        if (cause instanceof StreamingModularDrm.DrmRequestException) {
            return new DrmRequestError(cause);
        }
        if (e instanceof IOException || cause instanceof IOException) {
            return new ConnectivityError(cause == null ? e : cause);
        }
        return new UnknownError(e);
    }
}
