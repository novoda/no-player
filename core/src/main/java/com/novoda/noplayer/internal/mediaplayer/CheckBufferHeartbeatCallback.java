package com.novoda.noplayer.internal.mediaplayer;

import com.novoda.noplayer.internal.Heart;
import com.novoda.noplayer.Player;
import com.novoda.noplayer.model.VideoPosition;

public class CheckBufferHeartbeatCallback implements Heart.Heartbeat.Callback<Player> {

    private static final int FORCED_BUFFERING_BEATS_THRESHOLD = 4;

    private BufferListener bufferListener = BufferListener.NULL_IMPL;
    private VideoPosition previousPosition = VideoPosition.INVALID;
    private int beatsPlayed;

    public void bind(BufferListener bufferListener) {
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

        BufferListener NULL_IMPL = new BufferListener() {
            @Override
            public void onBufferStart() {
                // do nothing
            }

            @Override
            public void onBufferComplete() {
                // do nothing
            }
        };
    }
}
