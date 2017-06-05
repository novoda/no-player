package com.novoda.noplayer.mediaplayer;

import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.ERROR;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.IDLE;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PREPARING;

class PlaybackStateChecker {

    boolean isInPlaybackState(PlaybackState playbackState) {
        return playbackState != ERROR
                && playbackState != IDLE
                && playbackState != PREPARING;
    }

    enum PlaybackState {

        ERROR,
        IDLE,
        PREPARING,
        PREPARED,
        PLAYING,
        PAUSED,
        COMPLETED;

    }
}
