package com.novoda.noplayer.exoplayer.playererror;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.drm.DrmRequestException;

import java.io.IOException;

public final class ExoPlayerErrorFactory {

    private ExoPlayerErrorFactory() {
        // uninstantiable
    }

    public static Player.PlayerError errorFor(Exception e) {
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
        if (cause instanceof DrmRequestException) {
            return new DrmRequestError(cause);
        }
        if (e instanceof IOException || cause instanceof IOException) {
            return new ConnectivityError(cause == null ? e : cause);
        }
        return new UnknownError(e);
    }
}
