package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevel;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevelEventListener;

class DrmSecurityLevelForwarder implements DrmSecurityLevelEventListener {

    private final NoPlayer.DrmSecurityLevelCallback drmSecurityLevelCallback;

    DrmSecurityLevelForwarder(NoPlayer.DrmSecurityLevelCallback drmSecurityLevelCallback) {
        this.drmSecurityLevelCallback = drmSecurityLevelCallback;
    }

    @Override
    public void contentSecurityLevel(DrmSecurityLevel securityLevel) {
        drmSecurityLevelCallback.onSecurityLevelDetermined(securityLevel);
    }
}
