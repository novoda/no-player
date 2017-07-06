package com.novoda.noplayer.internal.mediaplayer;

enum AndroidMediaPlayerType {

    AWESOME("AwesomePlayer"),
    NU("NuPlayer"),
    UNKNOWN("Unknown");

    private final String name;

    AndroidMediaPlayerType(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
