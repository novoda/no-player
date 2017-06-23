package com.novoda.noplayer.drm;

import com.novoda.noplayer.drm.provision.ProvisionExecutor;
import com.novoda.noplayer.player.PlayerFactory;

public class DrmSessionCreatorFactory {

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
        switch (drmType) {
            case NONE:
                // Fall-through.
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
                return new NoDrmSessionCreator();
            default:
                throw PlayerFactory.UnableToCreatePlayerException.noDrmHandlerFor(drmType);
        }
    }
}
