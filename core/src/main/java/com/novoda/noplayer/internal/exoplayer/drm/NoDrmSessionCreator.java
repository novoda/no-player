package com.novoda.noplayer.internal.exoplayer.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import androidx.annotation.Nullable;

class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DefaultDrmSessionManager<FrameworkMediaCrypto> NO_DRM_SESSION = null;

    @Nullable
    @Override
    public DefaultDrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        return NO_DRM_SESSION;
    }
}
