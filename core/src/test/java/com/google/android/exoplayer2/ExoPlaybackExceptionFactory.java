package com.google.android.exoplayer2;

public class ExoPlaybackExceptionFactory {

    public static ExoPlaybackException createForUnexpected(RuntimeException exception) {
        return ExoPlaybackException.createForUnexpected(exception);
    }

}
