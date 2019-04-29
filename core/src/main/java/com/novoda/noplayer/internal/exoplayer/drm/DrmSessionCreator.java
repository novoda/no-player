package com.novoda.noplayer.internal.exoplayer.drm;

import android.support.annotation.Nullable;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.DrmSecurityLevel;
import com.novoda.noplayer.internal.utils.Optional;

import java.util.UUID;

public interface DrmSessionCreator {

    UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    @Nullable
    DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener,
                                                   DrmSecurityLevelEventListener drmSecurityLevelEventListener,
                                                   Optional<DrmSecurityLevel> forcedDrmSecurityLevel);
}
