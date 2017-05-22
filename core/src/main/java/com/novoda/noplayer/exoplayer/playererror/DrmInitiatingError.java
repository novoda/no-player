package com.novoda.noplayer.exoplayer.playererror;

import com.novoda.noplayer.Player;

public class DrmInitiatingError implements Player.PlayerError {

    private final int errorCode;
    private final Throwable cause;

    public static DrmInitiatingError newInstance(int errorCode, String message) {
        return new DrmInitiatingError(errorCode, new DrmErrorTrackingThrowable(message));
    }

    DrmInitiatingError(int errorCode, Throwable cause) {
        this.errorCode = errorCode;
        this.cause = cause;
    }

    @Override
    public String getType() {
        return String.valueOf(errorCode);
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    private static class DrmErrorTrackingThrowable extends Throwable {

        public DrmErrorTrackingThrowable(String detailMessage) {
            super(detailMessage);
        }
    }
}
