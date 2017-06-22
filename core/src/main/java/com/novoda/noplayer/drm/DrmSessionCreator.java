package com.novoda.noplayer.drm;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

public interface DrmSessionCreator {

    @Nullable
    DrmSessionManager<FrameworkMediaCrypto> create();

    class DrmSessionManagerCreationException extends RuntimeException {

        DrmSessionManagerCreationException(Throwable cause) {
            super(cause);
        }
    }
}
