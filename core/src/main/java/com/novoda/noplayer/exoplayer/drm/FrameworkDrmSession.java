package com.novoda.noplayer.exoplayer.drm;

import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

interface FrameworkDrmSession extends DrmSession<FrameworkMediaCrypto> {

    SessionId getSessionId();
}
