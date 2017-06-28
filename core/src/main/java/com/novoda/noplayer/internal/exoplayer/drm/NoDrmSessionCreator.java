package com.novoda.noplayer.internal.exoplayer.drm;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DrmSessionManager<FrameworkMediaCrypto> NO_DRM_SESSION = null;

    @Nullable
    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionManager.EventListener eventListener) {
        return NO_DRM_SESSION;
    }
}
