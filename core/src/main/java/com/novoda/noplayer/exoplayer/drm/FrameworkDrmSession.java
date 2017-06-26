package com.novoda.noplayer.exoplayer.drm;

import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.drm.SessionId;

interface FrameworkDrmSession extends DrmSession<FrameworkMediaCrypto> {

    SessionId getSessionId();
}
