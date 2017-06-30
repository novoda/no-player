package com.novoda.noplayer.internal.mediaplayer;

final class ErrorFormatter {

    private ErrorFormatter() {
    }

    static String formatMessage(int type, int extra) {
        return "Type: " + type + ", " + "Extra: " + extra;
    }

}
