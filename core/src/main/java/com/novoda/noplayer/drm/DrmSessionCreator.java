package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public interface DrmSessionCreator {

    DefaultDrmSessionManager<FrameworkMediaCrypto> create();
}
