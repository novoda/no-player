package com.novoda.noplayer.drm;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;

import java.util.HashMap;
import java.util.UUID;

public class StreamingDrmSessionCreator implements DrmSessionCreator {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;
    private static final DefaultDrmSessionManager.EventListener TODO_ERROR_EVENT_LISTENER = null;

    private final MediaDrmCallback mediaDrmCallback;
    private final FrameworkMediaDrmCreator frameworkMediaDrmCreator;
    private final Handler handler;

    public static StreamingDrmSessionCreator newInstance(MediaDrmCallback mediaDrmCallback, FrameworkMediaDrmCreator frameworkMediaDrmCreator) {
        Looper eventLooper = Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper();
        Handler handler = new Handler(eventLooper);
        return new StreamingDrmSessionCreator(mediaDrmCallback, frameworkMediaDrmCreator, handler);
    }

    private StreamingDrmSessionCreator(MediaDrmCallback mediaDrmCallback, FrameworkMediaDrmCreator frameworkMediaDrmCreator, Handler handler) {
        this.mediaDrmCallback = mediaDrmCallback;
        this.frameworkMediaDrmCreator = frameworkMediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        FrameworkMediaDrm frameworkMediaDrm = frameworkMediaDrmCreator.create(WIDEVINE_MODULAR_UUID);

        return new DefaultDrmSessionManager<>(
                WIDEVINE_MODULAR_UUID,
                frameworkMediaDrm,
                mediaDrmCallback,
                NO_OPTIONAL_PARAMETERS,
                handler,
                TODO_ERROR_EVENT_LISTENER
        );
    }
}
