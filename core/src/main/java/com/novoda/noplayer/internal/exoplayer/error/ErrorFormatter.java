package com.novoda.noplayer.internal.exoplayer.error;

final class ErrorFormatter {

    private ErrorFormatter() {
        // Static class.
    }

    static String formatMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}
