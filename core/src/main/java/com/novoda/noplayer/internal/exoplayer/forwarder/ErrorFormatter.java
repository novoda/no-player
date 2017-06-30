package com.novoda.noplayer.internal.exoplayer.forwarder;

final class ErrorFormatter {

    private ErrorFormatter() {
    }

    static String formatMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}
