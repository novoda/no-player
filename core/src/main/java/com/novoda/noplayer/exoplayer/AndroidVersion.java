package com.novoda.noplayer.exoplayer;

import android.os.Build;

class AndroidVersion {

    private int deviceOsVersion;

    static AndroidVersion newInstance() {
        return new AndroidVersion(Build.VERSION.SDK_INT);
    }

    AndroidVersion(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    boolean is21LollipopOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.LOLLIPOP;
    }
}
