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
    private DrmSecurityLevelFinder drmSecurityLevelFinder;

    DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm,
                              FrameworkMediaDrmCreator mediaDrmCreator,
                              Handler handler,
                              DrmSecurityLevelFinder drmSecurityLevelFinder) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.mediaDrmCreator = mediaDrmCreator;
        this.handler = handler;
        this.drmSecurityLevelFinder = drmSecurityLevelFinder;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener,
                                                          DrmSecurityLevelEventListener drmSecurityLevelEventListener) {

        return new LocalDrmSessionManager(
                downloadedModularDrm.getKeySetId(),
                mediaDrmCreator.create(WIDEVINE_MODULAR_UUID),
                WIDEVINE_MODULAR_UUID,
                handler,
                eventListener,
                drmSecurityLevelEventListener,
                drmSecurityLevelFinder
        );
    }
}
