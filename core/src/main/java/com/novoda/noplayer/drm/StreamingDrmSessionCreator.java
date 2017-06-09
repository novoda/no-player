package com.novoda.noplayer.drm;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;

import java.util.HashMap;
import java.util.UUID;

public class StreamingDrmSessionCreator implements DrmSessionCreator {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;
    private static final DefaultDrmSessionManager.EventListener TODO_ERROR_EVENT_LISTENER = null;

    private final MediaDrmCallback mediaDrmCallback;

    public StreamingDrmSessionCreator(MediaDrmCallback mediaDrmCallback) {
        this.mediaDrmCallback = mediaDrmCallback;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        Looper eventLooper = Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper();
        Handler mainHandler = new Handler(eventLooper);
        try {
            FrameworkMediaDrm frameworkMediaDrm = FrameworkMediaDrm.newInstance(WIDEVINE_MODULAR_UUID);
            return new DefaultDrmSessionManager<>(
                    WIDEVINE_MODULAR_UUID,
                    frameworkMediaDrm,
                    mediaDrmCallback,
                    NO_OPTIONAL_PARAMETERS,
                    mainHandler,
                    TODO_ERROR_EVENT_LISTENER
            );
        } catch (UnsupportedDrmException e) {
            throw new RuntimeException();
        }
    }
}
