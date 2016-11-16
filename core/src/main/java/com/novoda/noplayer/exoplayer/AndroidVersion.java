package com.novoda.noplayer.exoplayer;

import android.os.Build;

class AndroidVersion {

    private int deviceOsVersion;

    static AndroidVersion from(int deviceOsVersion) {
        return new AndroidVersion(deviceOsVersion);
    }

    private AndroidVersion(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    boolean is21LollipopOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.LOLLIPOP;
    }
}
