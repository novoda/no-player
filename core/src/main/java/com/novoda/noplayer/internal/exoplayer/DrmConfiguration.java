package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import androidx.annotation.Nullable;

interface DrmConfiguration {

    @Nullable
    DefaultDrmSessionManager<FrameworkMediaCrypto> configure(@Nullable DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager);

    enum NoOpDrmConfiguration implements DrmConfiguration {
        INSTANCE;

        @Nullable
        @Override
        public DefaultDrmSessionManager<FrameworkMediaCrypto> configure(@Nullable DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
            return drmSessionManager;
        }
    }

}
