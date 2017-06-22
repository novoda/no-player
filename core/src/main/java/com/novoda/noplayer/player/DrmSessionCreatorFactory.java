package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DownloadDrmSessionCreator;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmSessionCreator;
import com.novoda.noplayer.drm.DrmType;
import com.novoda.noplayer.drm.FrameworkMediaDrmCreator;
import com.novoda.noplayer.drm.NoDrmSessionCreator;
import com.novoda.noplayer.drm.ProvisioningModularDrmCallback;
import com.novoda.noplayer.drm.StreamingDrmSessionCreator;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;

class DrmSessionCreatorFactory {

    DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
        switch (drmType) {
            case NONE:
            case WIDEVINE_CLASSIC:
                return new NoDrmSessionCreator();
            case WIDEVINE_MODULAR_STREAM:
                ProvisionExecutor provisionExecutor = ProvisionExecutor.newInstance();
                ProvisioningModularDrmCallback mediaDrmCallback = new ProvisioningModularDrmCallback(
                        (StreamingModularDrm) drmHandler,
                        provisionExecutor
                );
                return StreamingDrmSessionCreator.newInstance(mediaDrmCallback, new FrameworkMediaDrmCreator());
            case WIDEVINE_MODULAR_DOWNLOAD:
                return new DownloadDrmSessionCreator((DownloadedModularDrm) drmHandler);
            default:
                throw PlayerFactory.UnableToCreatePlayerException.noDrmHandlerFor(drmType);
        }
    }
}
