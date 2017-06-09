package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;

public class DownloadDrmSessionCreator implements DrmSessionCreator {

    private final DownloadedModularDrm downloadedModularDrm;

    public DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm) {
        this.downloadedModularDrm = downloadedModularDrm;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create() {
        try {
            return new LocalDrmSession(downloadedModularDrm.getKeySetId());
        } catch (UnsupportedDrmException e) {
            throw new DrmSessionManagerCreationException(e);
        }
    }
}
