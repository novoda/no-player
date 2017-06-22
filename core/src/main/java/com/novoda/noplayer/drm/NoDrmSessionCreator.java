package com.novoda.noplayer.drm;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DrmSessionManager<FrameworkMediaCrypto> NO_DRM_SESSION = null;

    @Nullable
    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        return NO_DRM_SESSION;
    }
}
