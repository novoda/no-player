package com.novoda.support;

import android.os.Build;

public class DeviceAndroidVersion {

    private final int deviceOsVersion;

    public static DeviceAndroidVersion newInstance() {
        return new DeviceAndroidVersion(Build.VERSION.SDK_INT);
    }

    DeviceAndroidVersion(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public boolean is18JellyBeanOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public boolean is19KitKatOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.KITKAT;
    }

    public boolean is21LollipopOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.LOLLIPOP;
    }

    public boolean isPre21Lollipop() {
        return !is21LollipopOrOver();
    }

    public boolean is23MarshmallowOrOver() {
        return deviceOsVersion >= Build.VERSION_CODES.M;
    }

    public int getVersion() {
        return deviceOsVersion;
    }
}
