package com.novoda.utils;

import android.os.Build;

public class AndroidDeviceVersion {

    private final int sdkInt;

    public static AndroidDeviceVersion newInstance() {
        return new AndroidDeviceVersion(Build.VERSION.SDK_INT);
    }

    public AndroidDeviceVersion(int sdkInt) {
        this.sdkInt = sdkInt;
    }

    public boolean isJellyBeanEighteenOrAbove() {
        return sdkInt >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public int sdkInt() {
        return sdkInt;
    }
}
