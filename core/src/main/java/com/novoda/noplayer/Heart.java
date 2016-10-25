package com.novoda.noplayer;

import android.os.Handler;
import android.os.Looper;

public class Heart {

    private static final Time HEART_BEAT_FREQUENCY = Time.fromMillis(500);

    private final Handler handler;
    private final Heartbeat heartbeatAction;
    private final Time heartbeatFrequency;

    private boolean beating;

    public static Heart newInstance(Heartbeat onHeartbeat) {
        Handler handler = new Handler(Looper.getMainLooper());
        return new Heart(handler, onHeartbeat, HEART_BEAT_FREQUENCY);
    }

    Heart(Handler handler, Heartbeat onHeartbeat, Time heartbeatFrequency) {
        this.handler = handler;
        this.heartbeatAction = onHeartbeat;
        this.heartbeatFrequency = heartbeatFrequency;
    }

    public void startBeatingHeart() {
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
        handler.postDelayed(heartbeat, heartbeatFrequency.toMillis());
    }

    public void stopBeatingHeart() {
        beating = false;
        handler.removeCallbacks(heartbeat);
    }

    public void forceBeat() {
        handler.post(heartbeatAction);
    }

    public boolean isBeating() {
        return beating;
    }

    public static class Heartbeat<T> implements Runnable {

        private final Callback<T> callback;
        private final T object;

        public Heartbeat(Callback<T> callback, T object) {
            this.callback = callback;
            this.object = object;
        }

        @Override
        public void run() {
            callback.onBeat(object);
        }

        public interface Callback<T> {
            void onBeat(T object);
        }

    }

}
