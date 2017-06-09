package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public class NoDrmSessionCreator implements DrmSessionCreator {

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        return null;
    }
}
