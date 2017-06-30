package com.novoda.noplayer.internal.mediaplayer;

final class ErrorFormating {

    private ErrorFormating() {
    }

    static String formatMessage(int type, int extra) {
        return "Type: " + type + ", " + "Extra: " + extra;
    }

}
