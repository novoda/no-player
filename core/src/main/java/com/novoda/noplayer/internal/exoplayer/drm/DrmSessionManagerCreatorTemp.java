package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;

import java.util.HashMap;
import java.util.UUID;

public class DrmSessionManagerCreatorTemp {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final ProvisionExecutorCreator provisionExecutorCreator;
    private final FrameworkMediaDrmCreator frameworkMediaDrmCreator;
    private final Handler handler;

    public static DrmSessionManagerCreatorTemp newInstance() {
        ProvisionExecutorCreator provisionExecutorCreator = new ProvisionExecutorCreator();
        FrameworkMediaDrmCreator frameworkMediaDrmCreator = new FrameworkMediaDrmCreator();
        Handler handler = new Handler(Looper.getMainLooper());
        return new DrmSessionManagerCreatorTemp(provisionExecutorCreator, frameworkMediaDrmCreator, handler);
    }

    private DrmSessionManagerCreatorTemp(ProvisionExecutorCreator provisionExecutorCreator,
                                         FrameworkMediaDrmCreator frameworkMediaDrmCreator,
                                         Handler handler) {
        this.provisionExecutorCreator = provisionExecutorCreator;
        this.frameworkMediaDrmCreator = frameworkMediaDrmCreator;
        this.handler = handler;
    }

    public DefaultDrmSessionManager<FrameworkMediaCrypto> create(KeyRequestExecutor keyRequestExecutor, DefaultDrmSessionEventListener eventListener) {
        ProvisionExecutor provisionExecutor = provisionExecutorCreator.create();
        ProvisioningModularDrmCallbackTemp mediaDrmCallback = new ProvisioningModularDrmCallbackTemp(
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
