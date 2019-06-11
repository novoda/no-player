package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.DownloadedModularDrm;

import java.util.HashMap;

class DownloadDrmSessionCreator implements DrmSessionCreator {

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final MediaDrmCallback mediaDrmCallback;
    private final DownloadedModularDrm downloadedModularDrm;
    private final FrameworkMediaDrmCreator mediaDrmCreator;
    private final Handler handler;

    DownloadDrmSessionCreator(MediaDrmCallback mediaDrmCallback,
                              DownloadedModularDrm downloadedModularDrm,
                              FrameworkMediaDrmCreator mediaDrmCreator, Handler handler) {
        this.mediaDrmCallback = mediaDrmCallback;
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrmCreator = mediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        FrameworkMediaDrm frameworkMediaDrm = mediaDrmCreator.create(WIDEVINE_MODULAR_UUID);

        DefaultDrmSessionManager<FrameworkMediaCrypto> defaultDrmSessionManager = new DefaultDrmSessionManager<>(
                WIDEVINE_MODULAR_UUID,
                frameworkMediaDrm,
                mediaDrmCallback,
                NO_OPTIONAL_PARAMETERS
        );

        defaultDrmSessionManager.removeListener(eventListener);
        defaultDrmSessionManager.addListener(handler, eventListener);

        defaultDrmSessionManager.setMode(DefaultDrmSessionManager.MODE_QUERY, downloadedModularDrm.getKeySetId().asBytes());
        return defaultDrmSessionManager;
    }
}
