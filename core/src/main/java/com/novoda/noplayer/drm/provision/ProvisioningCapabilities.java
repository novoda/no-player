package com.novoda.noplayer.drm.provision;

import android.os.Build;

public class ProvisioningCapabilities {

    private final int deviceOsVersion;

    public static ProvisioningCapabilities newInstance() {
        return new ProvisioningCapabilities(Build.VERSION.SDK_INT);
    }

    private ProvisioningCapabilities(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    boolean canProvision() {
        return deviceOsVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}
