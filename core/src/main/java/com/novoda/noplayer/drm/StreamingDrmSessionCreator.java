package com.novoda.noplayer.drm;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;

import java.util.UUID;

public class StreamingDrmSessionCreator implements DrmSessionCreator {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    private final MediaDrmCallback mediaDrmCallback;

    public StreamingDrmSessionCreator(MediaDrmCallback mediaDrmCallback) {
        this.mediaDrmCallback = mediaDrmCallback;
    }

    @Override
    public DefaultDrmSessionManager<FrameworkMediaCrypto> create() {
        Looper eventLooper = Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper();
        Handler mainHandler = new Handler(eventLooper);
        try {
            FrameworkMediaDrm frameworkMediaDrm = FrameworkMediaDrm.newInstance(WIDEVINE_MODULAR_UUID);
            return new DefaultDrmSessionManager<>(
                    WIDEVINE_MODULAR_UUID,
                    frameworkMediaDrm,
                    mediaDrmCallback,
                    null,
                    mainHandler,
                    null
            );
        } catch (UnsupportedDrmException e) {
            throw new RuntimeException();
        }
    }
}
