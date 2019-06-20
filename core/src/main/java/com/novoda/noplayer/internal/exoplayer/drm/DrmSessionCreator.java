package com.novoda.noplayer.internal.exoplayer.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.UUID;

import androidx.annotation.Nullable;

public interface DrmSessionCreator {

    UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    @Nullable
    DefaultDrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener);
}
