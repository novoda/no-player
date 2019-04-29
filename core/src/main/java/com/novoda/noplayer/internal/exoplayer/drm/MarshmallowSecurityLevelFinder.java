package com.novoda.noplayer.internal.exoplayer.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaDrm;
import android.media.MediaDrmResetException;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.M)
class MarshmallowSecurityLevelFinder {

    private static final String KEY_SECURITY_LEVEL = "securityLevel";

    WidevineSecurityLevel findWidevineSecurityLevel(MediaDrm mediaDrm) {
        return readSecurityLevel(mediaDrm);
    }

    // KEY_SECURITY_LEVEL is an undocumented but valid property for MediaDrm
    @SuppressLint("WrongConstant")
    private WidevineSecurityLevel readSecurityLevel(MediaDrm mediaDrm) throws MediaDrmResetException {
        String deviceLevel = mediaDrm.getPropertyString(KEY_SECURITY_LEVEL);
        return WidevineSecurityLevel.fromString(deviceLevel);
    }
}
