package com.novoda.noplayer;

import com.novoda.noplayer.model.VideoDuration;

public interface PlayerState {

    boolean isPlaying();

    int videoWidth();

    int videoHeight();

    long playheadPositionInMillis();

    VideoDuration mediaDuration();

    int bufferPercentage();
}
