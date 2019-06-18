package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Build;
import android.os.Handler;

import com.novoda.noplayer.UnableToCreatePlayerException;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutorCreator;
import com.novoda.noplayer.internal.utils.AndroidDeviceVersion;

public class DrmSessionCreatorFactory {

    private final AndroidDeviceVersion androidDeviceVersion;
    private final ProvisionExecutorCreator provisionExecutorCreator;
    private final Handler handler;

    public DrmSessionCreatorFactory(AndroidDeviceVersion androidDeviceVersion, ProvisionExecutorCreator provisionExecutorCreator, Handler handler) {
        this.androidDeviceVersion = androidDeviceVersion;
        this.provisionExecutorCreator = provisionExecutorCreator;
        this.handler = handler;
    }

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) throws DrmSessionCreatorException {
        switch (drmType) {
            case NONE:
                // Fall-through.
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
                assertThatApiLevelIsJellyBeanEighteenOrAbove(drmType);
                return createModularStream((StreamingModularDrm) drmHandler);
            case WIDEVINE_MODULAR_DOWNLOAD:
                assertThatApiLevelIsJellyBeanEighteenOrAbove(drmType);
                return createModularDownload((DownloadedModularDrm) drmHandler);
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

    private DrmSessionCreator createModularStream(StreamingModularDrm drmHandler) {
        ProvisionExecutor provisionExecutor = provisionExecutorCreator.create();
        ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                drmHandler,
                provisionExecutor
        );
        FrameworkMediaDrmCreator mediaDrmCreator = new FrameworkMediaDrmCreator();
        return new StreamingDrmSessionCreator(mediaDrmCallback, mediaDrmCreator, handler);
    }

    private DownloadDrmSessionCreator createModularDownload(DownloadedModularDrm drmHandler) {
        FrameworkMediaDrmCreator mediaDrmCreator = new FrameworkMediaDrmCreator();
        return new DownloadDrmSessionCreator(drmHandler, mediaDrmCreator, handler);
    }
}
