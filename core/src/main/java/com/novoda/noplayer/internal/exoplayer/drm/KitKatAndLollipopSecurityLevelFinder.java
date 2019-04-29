package com.novoda.noplayer.internal.exoplayer.drm;

import android.annotation.SuppressLint;
import android.media.MediaDrm;

class KitKatAndLollipopSecurityLevelFinder {

    private static final String KEY_SECURITY_LEVEL = "securityLevel";

    // KEY_SECURITY_LEVEL is an undocumented but valid property for MediaDrm
    @SuppressLint("WrongConstant")
    DrmSecurityLevel findSecurityLevel(MediaDrm mediaDrm) {
        String deviceLevel = mediaDrm.getPropertyString(KEY_SECURITY_LEVEL);
        return DrmSecurityLevel.fromString(deviceLevel);
    }
}
