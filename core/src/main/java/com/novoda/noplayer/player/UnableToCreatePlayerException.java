package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmType;
import com.novoda.utils.AndroidDeviceVersion;

public class UnableToCreatePlayerException extends RuntimeException {

    static UnableToCreatePlayerException unhandledDrmType(DrmType drmType) {
        return new UnableToCreatePlayerException("Unhandled DrmType: " + drmType);
    }

    public static UnableToCreatePlayerException noDrmHandlerFor(DrmType drmType) {
        return new UnableToCreatePlayerException("No DrmHandler for DrmType: " + drmType);
    }

    static UnableToCreatePlayerException unhandledPlayerType(PlayerType playerType) {
        return new UnableToCreatePlayerException("Unhandled player type: " + playerType.name());
    }

    public static UnableToCreatePlayerException deviceDoesNotMeetTargetApiException(DrmType drmType,
                                                                                    int targetApiLevel,
                                                                                    AndroidDeviceVersion actualApiLevel) {
        return new UnableToCreatePlayerException(
                "Device must be target: "
                        + targetApiLevel
                        + " but was: "
                        + actualApiLevel.sdkInt()
                        + " for DRM type: "
                        + drmType.name()
        );
    }

    UnableToCreatePlayerException(String reason) {
        super(reason);
    }
}
