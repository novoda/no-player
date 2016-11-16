package com.novoda.noplayer;

import android.os.Build;

public class AndroidVersion {

    private static int deviceOsVersion;

    public static AndroidVersion from(int deviceOsVersion) {
        return new AndroidVersion(deviceOsVersion);
    }

    private AndroidVersion(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public boolean is21LollipopOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.LOLLIPOP;
    }
}
