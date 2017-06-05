package com.novoda.noplayer.mediaplayer;

enum PlaybackState {

    ERROR,
    IDLE,
    PREPARING,
    PREPARED,
    PLAYING,
    PAUSED,
    COMPLETED;

    boolean isInPlaybackState() {
        return this != ERROR
                && this != IDLE
                && this != PREPARING;
    }

}
