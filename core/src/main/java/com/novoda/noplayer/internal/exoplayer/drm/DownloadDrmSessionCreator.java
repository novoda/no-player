package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;

import java.util.HashMap;

class DownloadDrmSessionCreator implements DrmSessionCreator {

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final DownloadedModularDrm downloadedModularDrm;
    private final ProvisionExecutor provisionExecutor;
    private final FrameworkMediaDrmCreator mediaDrmCreator;
    private final Handler handler;

    DownloadDrmSessionCreator(DownloadedModularDrm downloadedModularDrm,
                              ProvisionExecutor provisionExecutor,
                              FrameworkMediaDrmCreator mediaDrmCreator,
                              Handler handler) {
        this.downloadedModularDrm = downloadedModularDrm;
        this.provisionExecutor = provisionExecutor;
        this.mediaDrmCreator = mediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DefaultDrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                new KeyRequestExecutor() {
                    @Override
                    public byte[] executeKeyRequest(ModularDrmKeyRequest request) {
                        return downloadedModularDrm.getKeySetId().asBytes();
                    }
                },
                provisionExecutor
        );

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
