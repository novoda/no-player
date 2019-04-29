package com.novoda.noplayer.internal.exoplayer.drm;

import com.novoda.noplayer.DrmSecurityLevel;

public interface DrmSecurityLevelEventListener {

    void contentSecurityLevel(DrmSecurityLevel securityLevel);

}
