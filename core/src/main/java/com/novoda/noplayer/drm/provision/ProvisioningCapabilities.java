package com.novoda.noplayer.drm.provision;

import android.os.Build;

class ProvisioningCapabilities {

    private final int deviceOsVersion;

    public static ProvisioningCapabilities newInstance() {
        return new ProvisioningCapabilities(Build.VERSION.SDK_INT);
    }

    ProvisioningCapabilities(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public boolean canProvision() {
        return deviceOsVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}
