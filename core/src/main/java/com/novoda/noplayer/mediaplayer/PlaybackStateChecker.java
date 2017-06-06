package com.novoda.noplayer.mediaplayer;

import android.media.MediaPlayer;

import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.ERROR;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.IDLE;
import static com.novoda.noplayer.mediaplayer.PlaybackStateChecker.PlaybackState.PREPARING;

class PlaybackStateChecker {

    boolean isPlaying(MediaPlayer mediaPlayer, PlaybackState playbackState) {
        return isInPlaybackState(mediaPlayer, playbackState) && mediaPlayer.isPlaying();
    }

    boolean isInPlaybackState(MediaPlayer mediaPlayer, PlaybackState playbackState) {
        return mediaPlayer != null
                && playbackState != ERROR
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
