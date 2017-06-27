package com.novoda.noplayer.drm;

import android.os.Build;
import android.os.Handler;

import com.novoda.noplayer.drm.provision.HttpUrlConnectionPoster;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.drm.provision.ProvisioningCapabilities;
import com.novoda.noplayer.player.PlayerFactory;
import com.novoda.utils.AndroidDeviceVersion;

public class DrmSessionCreatorFactory {

    private final AndroidDeviceVersion androidDeviceVersion;
    private final Handler handler;

    public DrmSessionCreatorFactory(AndroidDeviceVersion androidDeviceVersion, Handler handler) {
        this.androidDeviceVersion = androidDeviceVersion;
        this.handler = handler;
    }

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
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

    private DrmSessionCreator createModularStream(StreamingModularDrm drmHandler) {
        HttpUrlConnectionPoster httpUrlConnectionPoster = new HttpUrlConnectionPoster();
        ProvisioningCapabilities capabilities = ProvisioningCapabilities.newInstance();
        ProvisionExecutor provisionExecutor = new ProvisionExecutor(httpUrlConnectionPoster, capabilities);
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
