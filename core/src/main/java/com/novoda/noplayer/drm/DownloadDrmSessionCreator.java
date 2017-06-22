package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

class DownloadDrmSessionCreator implements DrmSessionCreator {

    private final DownloadedModularDrm downloadedModularDrm;
    private final FrameworkMediaDrmCreator mediaDrmCreator;

    DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm, FrameworkMediaDrmCreator mediaDrmCreator) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrmCreator = mediaDrmCreator;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionManager.EventListener eventListener) {
        return LocalDrmSessionManager.newInstance(downloadedModularDrm.getKeySetId(), mediaDrmCreator.create(WIDEVINE_MODULAR_UUID), eventListener, WIDEVINE_MODULAR_UUID);
    }
}
