package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.DownloadedModularDrm;

class DownloadDrmSessionCreator implements DrmSessionCreator {

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
        return new LocalDrmSessionManager(
                mediaDrmCallback,
                downloadedModularDrm,
                mediaDrmCreator.create(WIDEVINE_MODULAR_UUID),
                WIDEVINE_MODULAR_UUID,
                handler,
                eventListener
        );
    }
}
