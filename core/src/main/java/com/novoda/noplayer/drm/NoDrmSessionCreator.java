package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public class NoDrmSessionCreator implements DrmSessionCreator {

    @Override
    public DefaultDrmSessionManager<FrameworkMediaCrypto> create() {
        return null;
    }
}
