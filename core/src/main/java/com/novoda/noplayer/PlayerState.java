package com.novoda.noplayer;

public interface PlayerState {

    boolean isPlaying();

    boolean isSetToPlayAdvert();

    boolean isSetToPlayContent();

    int videoWidth();

    int videoHeight();

    long positionInAdvertBreakInMillis();

    long advertBreakDurationInMillis();

    long playheadPositionInMillis();

    long mediaDurationInMillis();

    int bufferPercentage();
}
