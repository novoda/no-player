package com.novoda.noplayer.internal.exoplayer.drm;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.DrmSecurityLevel;
import com.novoda.noplayer.internal.utils.Optional;

class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DrmSessionManager<FrameworkMediaCrypto> NO_DRM_SESSION = null;

    @Nullable
    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener,
                                                          DrmSecurityLevelEventListener drmSecurityLevelEventListener,
                                                          Optional<DrmSecurityLevel> forcedDrmSecurityLevel) {
        return NO_DRM_SESSION;
    }
}
