package com.novoda.noplayer.drm;

import android.os.Build;

import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.utils.AndroidDeviceVersion;

public class DrmSessionCreatorFactory {

    private final AndroidDeviceVersion androidDeviceVersion;

    public DrmSessionCreatorFactory(AndroidDeviceVersion androidDeviceVersion) {
        this.androidDeviceVersion = androidDeviceVersion;
    }

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
        switch (drmType) {
            case NONE:
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
                assertThatApiLevelIsJellyBeanEighteenOrAbove(drmType);
                ProvisionExecutor provisionExecutor = ProvisionExecutor.newInstance();
                ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                        (StreamingModularDrm) drmHandler,
                        provisionExecutor
                );
                return StreamingDrmSessionCreator.newInstance(mediaDrmCallback, new FrameworkMediaDrmCreator());
            case WIDEVINE_MODULAR_DOWNLOAD:
                assertThatApiLevelIsJellyBeanEighteenOrAbove(drmType);
                return new DownloadDrmSessionCreator((DownloadedModularDrm) drmHandler, new FrameworkMediaDrmCreator());
            default:
                throw PlayerFactory.UnableToCreatePlayerException.noDrmHandlerFor(drmType);
        }
    }

    private void assertThatApiLevelIsJellyBeanEighteenOrAbove(DrmType drmType) {
        if (androidDeviceVersion.isJellyBeanEighteenOrAbove()) {
            return;
        }
        throw PlayerFactory.UnableToCreatePlayerException.deviceDoesNotMeetTargetApiException(
                drmType,
                Build.VERSION_CODES.JELLY_BEAN_MR2,
                androidDeviceVersion
        );
    }
}
