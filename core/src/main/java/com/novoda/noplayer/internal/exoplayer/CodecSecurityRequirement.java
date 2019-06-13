package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.drm.DrmInitData;

import androidx.annotation.Nullable;

final class CodecSecurityRequirement {

    private boolean secureCodecsRequired;

    static CodecSecurityRequirement getInstance() {
        return LazySingleton.PROVIDER;
    }

    void updateSecureCodecsRequirement(@Nullable DrmInitData drmInitData) {
        secureCodecsRequired = drmInitData != null;
    }

    boolean secureCodecsRequired() {
        return secureCodecsRequired;
    }

    private static class LazySingleton {

        private static final CodecSecurityRequirement PROVIDER = new CodecSecurityRequirement();
    }
}
