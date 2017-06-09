package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public interface DrmSessionCreator {

    DrmSessionManager<FrameworkMediaCrypto> create();
}