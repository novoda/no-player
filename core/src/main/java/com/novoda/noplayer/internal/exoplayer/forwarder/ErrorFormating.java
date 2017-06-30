package com.novoda.noplayer.internal.exoplayer.forwarder;

final class ErrorFormating {

    private ErrorFormating() {
    }

    static String formatMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }

}
