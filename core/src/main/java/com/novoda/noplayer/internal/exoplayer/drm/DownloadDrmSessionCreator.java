package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.drm.DownloadedModularDrm;

class DownloadDrmSessionCreator implements DrmSessionCreator {

    private final DownloadedModularDrm downloadedModularDrm;
    private final FrameworkMediaDrmCreator mediaDrmCreator;
    private final Handler handler;

    DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm, FrameworkMediaDrmCreator mediaDrmCreator, Handler handler) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrmCreator = mediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        return new LocalDrmSessionManager(
                downloadedModularDrm.getKeySetId(),
                mediaDrmCreator.create(WIDEVINE_MODULAR_UUID),
                WIDEVINE_MODULAR_UUID,
                handler,
                eventListener
        );
    }
}
