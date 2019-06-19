package com.novoda.noplayer.internal.exoplayer.drm;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DrmSessionManager<FrameworkMediaCrypto> NO_DRM_SESSION = null;

    @Nullable
    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        return NO_DRM_SESSION;
    }
}
