package com.novoda.noplayer;

public class AndroidVersion {

    private static int deviceOsVersion;

    public static AndroidVersion from(int deviceOsVersion) {
        return new AndroidVersion(deviceOsVersion);
    }

    private AndroidVersion(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public int deviceOsVersion() {
        return deviceOsVersion;
    }
}
