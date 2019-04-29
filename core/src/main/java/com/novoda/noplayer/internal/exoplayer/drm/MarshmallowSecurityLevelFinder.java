package com.novoda.noplayer.internal.exoplayer.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaDrmResetException;
import android.os.Build;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.novoda.noplayer.DrmSecurityLevel;

@TargetApi(Build.VERSION_CODES.M)
class MarshmallowSecurityLevelFinder {

    private static final String KEY_SECURITY_LEVEL = "securityLevel";

    // KEY_SECURITY_LEVEL is an undocumented but valid property for MediaDrm
    @SuppressLint("WrongConstant")
    DrmSecurityLevel findSecurityLevel(ExoMediaDrm mediaDrm) throws MediaDrmResetException {
        String deviceLevel = mediaDrm.getPropertyString(KEY_SECURITY_LEVEL);
        return DrmSecurityLevel.fromString(deviceLevel);
    }

}
