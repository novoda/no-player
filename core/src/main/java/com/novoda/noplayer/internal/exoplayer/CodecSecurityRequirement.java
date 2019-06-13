package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.drm.DrmInitData;

import androidx.annotation.Nullable;

final class CodecSecurityRequirement {

    private boolean secureCodecsRequired;

    void updateSecureCodecsRequirement(@Nullable DrmInitData drmInitData) {
        secureCodecsRequired = drmInitData != null;
    }

    boolean secureCodecsRequired() {
        return secureCodecsRequired;
    }

}
