package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.novoda.noplayer.drm.DrmRequestException;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.exoplayer.playererror.ConnectivityError;
import com.novoda.noplayer.exoplayer.playererror.DrmRequestError;
import com.novoda.noplayer.exoplayer.playererror.InvalidResponseCodeError;
import com.novoda.noplayer.exoplayer.playererror.MalformedContentError;
import com.novoda.noplayer.exoplayer.playererror.UnknownError;

import java.io.IOException;

final class ExoPlayerErrorFactory {

    private ExoPlayerErrorFactory() {
        // uninstantiable
    }

    static Player.PlayerError errorFor(Exception e) {
        if (e instanceof HttpDataSource.InvalidResponseCodeException) {
            return new InvalidResponseCodeError(e);
        }
        if (e instanceof ParserException) {
            return new MalformedContentError(e);
        }

        Throwable cause = e.getCause();
        if (cause instanceof DrmRequestException) {
            return new DrmRequestError(cause);
        }
        if (e instanceof IOException || cause instanceof IOException) {
            return new ConnectivityError(cause == null ? e : cause);
        }

        return new UnknownError(e);
    }
}
