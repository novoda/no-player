package com.novoda.noplayer;

public interface PlayerState {

    boolean isPlaying();

    int videoWidth();

    int videoHeight();

    long playheadPositionInMillis();

    long mediaDurationInMillis();

    int bufferPercentage();
}
