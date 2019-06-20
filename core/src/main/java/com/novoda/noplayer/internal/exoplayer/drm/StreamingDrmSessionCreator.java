package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;

import java.util.HashMap;
import java.util.UUID;

class StreamingDrmSessionCreator implements DrmSessionCreator {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final KeyRequestExecutor keyRequestExecutor;
    private final ProvisionExecutor provisionExecutor;
    private final FrameworkMediaDrmCreator frameworkMediaDrmCreator;
    private final Handler handler;

    StreamingDrmSessionCreator(KeyRequestExecutor keyRequestExecutor,
                               ProvisionExecutor provisionExecutor,
                               FrameworkMediaDrmCreator frameworkMediaDrmCreator,
                               Handler handler) {
        this.keyRequestExecutor = keyRequestExecutor;
        this.provisionExecutor = provisionExecutor;
        this.frameworkMediaDrmCreator = frameworkMediaDrmCreator;
        this.handler = handler;
    }

    public DefaultDrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                keyRequestExecutor,
                provisionExecutor
        );

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
