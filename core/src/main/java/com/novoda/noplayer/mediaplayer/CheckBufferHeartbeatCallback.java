package com.novoda.noplayer.mediaplayer;

import com.novoda.noplayer.Heart;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.VideoPosition;

@SuppressWarnings("PMD.RedundantFieldInitializer")
        // we're being very explicit with our default field values, not a bad thing!
class CheckBufferHeartbeatCallback implements Heart.Heartbeat.Callback<Player> {

    private static final int FORCED_BUFFERING_BEATS_THRESHOLD = 4;
    private final BufferListener bufferListener;

    private VideoPosition previousPosition = VideoPosition.INVALID;
    private int beatsPlayed;

    CheckBufferHeartbeatCallback(BufferListener bufferListener) {
        this.bufferListener = bufferListener;
    }

    @Override
    public void onBeat(Player player) {
        if (mediaPlayerIsUnavailable(player)) {
            stopBuffering();
            return;
        }

        VideoPosition currentPosition = player.getPlayheadPosition();
        if (positionNotUpdating(currentPosition)) {
            beatsPlayed = 0;
            startBuffering();
        } else {
            previousPosition = currentPosition;
            beatsPlayed++;
            if (beatsPlayed > FORCED_BUFFERING_BEATS_THRESHOLD) {
                stopBuffering();
            }
        }
    }

    private boolean positionNotUpdating(VideoPosition currentPosition) {
        return currentPosition.equals(previousPosition);
    }

    private void stopBuffering() {
        bufferListener.onBufferComplete();
    }

    private void startBuffering() {
        bufferListener.onBufferStart();
    }

    private boolean mediaPlayerIsUnavailable(Player player) {
        try {
            return !player.isPlaying();
        } catch (IllegalStateException e) {
            // The mediaplayer has not been initialized or has been released
            return true;
        }
    }

    public interface BufferListener {

        void onBufferStart();

        void onBufferComplete();
    }
}
