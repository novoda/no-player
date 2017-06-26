package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.UUID;

class DownloadDrmSessionCreator implements DrmSessionCreator {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    private final DownloadedModularDrm downloadedModularDrm;
    private final FrameworkMediaDrmCreator mediaDrmCreator;

    DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm, FrameworkMediaDrmCreator mediaDrmCreator) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrmCreator = mediaDrmCreator;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        return new LocalDrmSessionManager(downloadedModularDrm.getKeySetId(), mediaDrmCreator.create(WIDEVINE_MODULAR_UUID));
    }
}
