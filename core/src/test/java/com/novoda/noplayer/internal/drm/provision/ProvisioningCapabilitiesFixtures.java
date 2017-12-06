package com.novoda.noplayer.internal.drm.provision;

import android.os.Build;

public final class ProvisioningCapabilitiesFixtures {

    public static ProvisioningCapabilitiesFixtures aProvisioningCapabilities() {
        return new ProvisioningCapabilitiesFixtures();
    }

    private ProvisioningCapabilitiesFixtures() {
        // Not instantiable
    }

    public ProvisioningCapabilities thatCanProvision() {
        return new ProvisioningCapabilities(Build.VERSION_CODES.JELLY_BEAN_MR2);
    }

    public ProvisioningCapabilities thatCannotProvision() {
        return new ProvisioningCapabilities(Build.VERSION_CODES.JELLY_BEAN_MR1);
    }
}
