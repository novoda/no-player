package com.novoda.noplayer.internal.drm.provision;

import android.os.Build;

public class UnableToProvisionException extends Exception {

    UnableToProvisionException() {
        super("Device is : " + Build.VERSION.SDK_INT
                + ", which is does not support provisioning, "
                + Build.VERSION_CODES.JELLY_BEAN_MR2
                + " and higher is required");
    }
}
