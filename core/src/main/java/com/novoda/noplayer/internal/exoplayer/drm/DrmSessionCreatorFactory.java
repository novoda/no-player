package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Build;
import android.os.Handler;

import com.novoda.noplayer.UnableToCreatePlayerException;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;
import com.novoda.noplayer.model.KeySetId;

import androidx.annotation.Nullable;

public class DrmSessionCreatorFactory {

    private final AndroidDeviceVersion androidDeviceVersion;
    private final ProvisionExecutorCreator provisionExecutorCreator;
    private final Handler handler;

    public DrmSessionCreatorFactory(AndroidDeviceVersion androidDeviceVersion, ProvisionExecutorCreator provisionExecutorCreator, Handler handler) {
        this.androidDeviceVersion = androidDeviceVersion;
        this.provisionExecutorCreator = provisionExecutorCreator;
        this.handler = handler;
    }

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler, @Nullable KeySetId keySetId) throws DrmSessionCreatorException {
        switch (drmType) {
            case NONE:
                // Fall-through.
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
            case WIDEVINE_MODULAR_DOWNLOAD:
                assertThatApiLevelIsJellyBeanEighteenOrAbove(drmType);
                return createReworkedDrm((KeyRequestExecutor) drmHandler, keySetId);
            default:
                throw DrmSessionCreatorException.noDrmHandlerFor(drmType);
        }
    }

    private void assertThatApiLevelIsJellyBeanEighteenOrAbove(DrmType drmType) {
        if (androidDeviceVersion.isJellyBeanEighteenOrAbove()) {
            return;
        }
        throw UnableToCreatePlayerException.deviceDoesNotMeetTargetApiException(
                drmType,
                Build.VERSION_CODES.JELLY_BEAN_MR2,
                androidDeviceVersion
        );
    }

    private DrmSessionCreator createModularStream(KeyRequestExecutor drmHandler) {
        ProvisionExecutor provisionExecutor = provisionExecutorCreator.create();
        FrameworkMediaDrmCreator mediaDrmCreator = new FrameworkMediaDrmCreator();
        return new StreamingDrmSessionCreator(drmHandler, provisionExecutor, mediaDrmCreator, handler);
    }

    private DownloadDrmSessionCreator createModularDownload(DownloadedModularDrm drmHandler) {
        FrameworkMediaDrmCreator mediaDrmCreator = new FrameworkMediaDrmCreator();
        ProvisionExecutor provisionExecutor = provisionExecutorCreator.create();
        return new DownloadDrmSessionCreator(drmHandler, provisionExecutor, mediaDrmCreator, handler);
    }

    private DrmSessionCreatorWithFallback createReworkedDrm(KeyRequestExecutor keyRequestExecutor, @Nullable KeySetId keySetId) {
        ProvisionExecutor provisionExecutor = provisionExecutorCreator.create();
        FrameworkMediaDrmCreator mediaDrmCreator = new FrameworkMediaDrmCreator();
        return new DrmSessionCreatorWithFallback(keyRequestExecutor, provisionExecutor, keySetId, mediaDrmCreator, handler);
    }
}
