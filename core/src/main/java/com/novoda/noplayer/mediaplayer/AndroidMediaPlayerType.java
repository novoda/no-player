package com.novoda.noplayer.mediaplayer;

public enum AndroidMediaPlayerType {

    AWESOME("AwesomePlayer"),
    NU("NuPlayer"),
    UNKNOWN("Unknown");

    private final String name;

    AndroidMediaPlayerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
