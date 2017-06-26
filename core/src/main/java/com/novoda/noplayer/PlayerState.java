package com.novoda.noplayer;

import com.novoda.noplayer.model.VideoDuration;
import com.novoda.noplayer.model.VideoPosition;

public interface PlayerState {

    boolean isPlaying();

    int getVideoWidth();

    int getVideoHeight();

    VideoPosition getPlayheadPosition();

    VideoDuration getMediaDuration();

    int getBufferPercentage();

}
