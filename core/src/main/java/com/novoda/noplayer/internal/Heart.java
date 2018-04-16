package com.novoda.noplayer.internal;

import android.os.Handler;

import com.novoda.noplayer.NoPlayer;

@SuppressWarnings("checkstyle:FinalClass")  // We cannot make it final as we need to mock it in tests
public class Heart {

    private static final long HEART_BEAT_FREQUENCY_IN_MILLIS = 500;

    private final Handler handler;
    private final long heartbeatFrequency;

    private Heartbeat heartbeatAction;

    private boolean beating;

    public static Heart newInstance(Handler handler) {
        return new Heart(handler, HEART_BEAT_FREQUENCY_IN_MILLIS);
    }

    private Heart(Handler handler, long heartbeatFrequencyInMillis) {
        this.handler = handler;
        this.heartbeatFrequency = heartbeatFrequencyInMillis;
    }

    public void bind(Heartbeat onHeartbeat) {
        this.heartbeatAction = onHeartbeat;
    }

    public void startBeatingHeart() {
        if (heartbeatAction == null) {
            throw new IllegalStateException("You must call bind() with a valid non-null " + Heartbeat.class.getSimpleName());
        }
        stopBeatingHeart();
        beating = true;
        handler.post(heartbeat);
    }

    private final Runnable heartbeat = new Runnable() {
        @Override
        public void run() {
            handler.post(heartbeatAction);
            scheduleNextBeat();
        }
    };

    private void scheduleNextBeat() {
        handler.postDelayed(heartbeat, heartbeatFrequency);
    }

    public void stopBeatingHeart() {
        beating = false;
        handler.removeCallbacks(heartbeat);
    }

    public void forceBeat() {
        if (heartbeatAction == null) {
            throw new IllegalStateException("You must call bind() with a valid non-null " + Heartbeat.class.getSimpleName());
        }
        handler.post(heartbeatAction);
    }

    public boolean isBeating() {
        return beating;
    }

    public static class Heartbeat implements Runnable {

        private final NoPlayer.HeartbeatCallback callback;
        private final NoPlayer player;

        public Heartbeat(NoPlayer.HeartbeatCallback callback, NoPlayer player) {
            this.callback = callback;
            this.player = player;
        }

        @Override
        public void run() {
            if (player.isPlaying()) {
                callback.onBeat(player);
            }
        }
    }
}
