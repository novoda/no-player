package com.novoda.noplayer;

public interface PlayerState {

    boolean isPlaying();

    int getVideoWidth();

    int getVideoHeight();

    VideoPosition getPlayheadPosition();

    VideoDuration getMediaDuration();

    int getBufferPercentage();

}
