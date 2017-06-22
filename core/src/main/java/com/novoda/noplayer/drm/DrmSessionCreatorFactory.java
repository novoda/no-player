package com.novoda.noplayer.drm;

import com.novoda.noplayer.drm.provision.ProvisionExecutor;

public class DrmSessionCreatorFactory {

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
        StreamingModularDrm streamingModularDrm = (StreamingModularDrm) drmHandler;
        ProvisioningModularDrmCallback provisioningModularDrmCallback = new ProvisioningModularDrmCallback(streamingModularDrm, ProvisionExecutor.newInstance());
        return StreamingDrmSessionCreator.newInstance(provisioningModularDrmCallback, new FrameworkMediaDrmCreator());
    }
}
