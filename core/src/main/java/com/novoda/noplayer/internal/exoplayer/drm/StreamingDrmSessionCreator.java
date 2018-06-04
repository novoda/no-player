package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;

import java.util.HashMap;

class StreamingDrmSessionCreator implements DrmSessionCreator {

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final MediaDrmCallback mediaDrmCallback;
    private final FrameworkMediaDrmCreator frameworkMediaDrmCreator;
    private final Handler handler;

    StreamingDrmSessionCreator(MediaDrmCallback mediaDrmCallback, FrameworkMediaDrmCreator frameworkMediaDrmCreator, Handler handler) {
        this.mediaDrmCallback = mediaDrmCallback;
        this.frameworkMediaDrmCreator = frameworkMediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        FrameworkMediaDrm frameworkMediaDrm = frameworkMediaDrmCreator.create(WIDEVINE_MODULAR_UUID);

        DefaultDrmSessionManager<FrameworkMediaCrypto> defaultDrmSessionManager = new DefaultDrmSessionManager<>(
                WIDEVINE_MODULAR_UUID,
                frameworkMediaDrm,
                mediaDrmCallback,
                NO_OPTIONAL_PARAMETERS
        );
        defaultDrmSessionManager.removeListener(eventListener);
        defaultDrmSessionManager.addListener(handler, eventListener);

        return defaultDrmSessionManager;
    }
}
