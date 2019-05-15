package com.novoda.noplayer.internal.drm.provision;

import android.os.Build;

import androidx.annotation.VisibleForTesting;

class ProvisioningCapabilities {

    private final int deviceOsVersion;

    static ProvisioningCapabilities newInstance() {
        return new ProvisioningCapabilities(Build.VERSION.SDK_INT);
    }

    @VisibleForTesting
    ProvisioningCapabilities(int deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    boolean canProvision() {
        return deviceOsVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}
