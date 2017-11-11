package com.novoda.noplayer.internal.mediaplayer;

import com.novoda.noplayer.NoPlayer;

public class CheckBufferHeartbeatCallback implements NoPlayer.HeartbeatCallback {

    private static final int FORCED_BUFFERING_BEATS_THRESHOLD = 4;

    private BufferListener bufferListener = BufferListener.NULL_IMPL;
    private long previousPositionInMillis = -1;
    private int beatsPlayed;

    public void bind(BufferListener bufferListener) {
        this.bufferListener = bufferListener;
    }

    @Override
    public void onBeat(NoPlayer player) {
        if (mediaPlayerIsUnavailable(player)) {
            stopBuffering();
            return;
        }

        long currentPositionInMillis = player.playheadPositionInMillis();
        if (positionNotUpdating(currentPositionInMillis)) {
            beatsPlayed = 0;
            startBuffering();
        } else {
            previousPositionInMillis = currentPositionInMillis;
            beatsPlayed++;
            if (beatsPlayed > FORCED_BUFFERING_BEATS_THRESHOLD) {
                stopBuffering();
            }
        }
    }

    private boolean positionNotUpdating(long currentPositionInMillis) {
        return currentPositionInMillis == previousPositionInMillis;
    }

    private void stopBuffering() {
        bufferListener.onBufferComplete();
    }

    private void startBuffering() {
        bufferListener.onBufferStart();
    }

    private boolean mediaPlayerIsUnavailable(NoPlayer player) {
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
