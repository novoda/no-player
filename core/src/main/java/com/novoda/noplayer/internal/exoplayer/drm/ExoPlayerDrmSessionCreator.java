package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;
import android.util.Pair;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.model.KeySetId;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

class ExoPlayerDrmSessionCreator implements DrmSessionCreator {

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;
    private static final String NO_DEFAULT_LICENSE_URL = "";
    private static final HttpDataSource.Factory NO_DATA_SOURCE_FACTORY = null;

    private final KeyRequestExecutor keyRequestExecutor;
    private final ProvisionExecutor provisionExecutor;
    @Nullable
    private final KeySetId keySetId;
    private final FrameworkMediaDrmCreator mediaDrmCreator;
    private final Handler handler;

    ExoPlayerDrmSessionCreator(KeyRequestExecutor keyRequestExecutor,
                               ProvisionExecutor provisionExecutor,
                               @Nullable KeySetId keySetId,
                               FrameworkMediaDrmCreator mediaDrmCreator,
                               Handler handler) {
        this.keyRequestExecutor = keyRequestExecutor;
        this.provisionExecutor = provisionExecutor;
        this.keySetId = keySetId;
        this.mediaDrmCreator = mediaDrmCreator;
        this.handler = handler;
    }

    @Override
    public DefaultDrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener) {
        ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                keyRequestExecutor,
                provisionExecutor
        );

        FrameworkMediaDrm frameworkMediaDrm = mediaDrmCreator.create(WIDEVINE_MODULAR_UUID);
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = new DefaultDrmSessionManager<>(
                WIDEVINE_MODULAR_UUID,
                frameworkMediaDrm,
                mediaDrmCallback,
                NO_OPTIONAL_PARAMETERS
        );

        if (keySetId != null) {
            checkExpiryOfKeySetId(eventListener, drmSessionManager);
        }

        drmSessionManager.removeListener(eventListener);
        drmSessionManager.addListener(handler, eventListener);

        return drmSessionManager;
    }

    private void checkExpiryOfKeySetId(DefaultDrmSessionEventListener eventListener,
                                       DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        try {
            OfflineLicenseHelper<FrameworkMediaCrypto> offlineLicenseHelper = OfflineLicenseHelper.newWidevineInstance(
                    NO_DEFAULT_LICENSE_URL,
                    NO_DATA_SOURCE_FACTORY
            );

            byte[] rawKeySetId = keySetId.asBytes();
            Pair<Long, Long> licenseDurationRemainingSec = offlineLicenseHelper.getLicenseDurationRemainingSec(rawKeySetId);
            Long first = licenseDurationRemainingSec.first;
            if (first > TimeUnit.HOURS.toSeconds(1)) {
                drmSessionManager.setMode(DefaultDrmSessionManager.MODE_QUERY, rawKeySetId);
            }

        } catch (UnsupportedDrmException | DrmSession.DrmSessionException e) {
            eventListener.onDrmSessionManagerError(e);
        }
    }
}
