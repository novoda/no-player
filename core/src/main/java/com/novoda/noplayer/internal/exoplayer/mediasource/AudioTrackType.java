package com.novoda.noplayer.internal.exoplayer.mediasource;

public enum AudioTrackType {

    MAIN(1),
    ALTERNATIVE(0),
    UNKNOWN(-1);

    private final int selectionFlag;

    AudioTrackType(int selectionFlag) {
        this.selectionFlag = selectionFlag;
    }

    static AudioTrackType from(int selectionFlag) {
        for (AudioTrackType audioTrackType : AudioTrackType.values()) {
            if (audioTrackType.selectionFlag == selectionFlag) {
                return audioTrackType;
            }
        }
        return UNKNOWN;
    }
}
