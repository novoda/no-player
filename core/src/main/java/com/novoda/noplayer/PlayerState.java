package com.novoda.noplayer;

public interface PlayerState {

    boolean isPlaying();

    int getVideoWidth();

    int getVideoHeight();

    Time getPlayheadPosition();

    Time getMediaDuration();

    int getBufferPercentage();

}
