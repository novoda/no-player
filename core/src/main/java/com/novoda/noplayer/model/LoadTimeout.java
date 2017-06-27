package com.novoda.noplayer.model;

import android.os.Handler;

import com.novoda.noplayer.Clock;
import com.novoda.noplayer.Player;

public class LoadTimeout {

    private static final int DELAY_MILLIS = 1000;

    private final Clock clock;
    private final Handler handler;

    private long startTime;
    private long endTime;
    private Player.LoadTimeoutCallback loadTimeoutCallback;

    public LoadTimeout(Clock clock, Handler handler) {
        this.clock = clock;
        this.handler = handler;
    }

    public void start(Timeout timeout, Player.LoadTimeoutCallback loadTimeoutCallback) {
        cancel();
        this.loadTimeoutCallback = loadTimeoutCallback;
        startTime = clock.getCurrentTime();
        endTime = startTime + timeout.inMillis();
        handler.post(loadTimeoutCheck);
    }

    private final Runnable loadTimeoutCheck = new Runnable() {
        @Override
        public void run() {
            if (clock.getCurrentTime() >= endTime) {
                loadTimeoutCallback.onLoadTimeout();
                cancel();
            } else {
                handler.postDelayed(this, DELAY_MILLIS);
            }
        }
    };

    public void cancel() {
        startTime = 0;
        loadTimeoutCallback = Player.LoadTimeoutCallback.NULL_IMPL;
        handler.removeCallbacks(loadTimeoutCheck);
    }

}
