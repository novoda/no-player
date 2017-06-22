package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public interface FrameworkDrmSession extends DrmSession<FrameworkMediaCrypto> {

    byte[] getSessionId();
}
