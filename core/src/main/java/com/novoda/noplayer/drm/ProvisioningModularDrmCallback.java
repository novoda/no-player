package com.novoda.noplayer.drm;

import android.media.MediaDrm;

import com.novoda.noplayer.drm.provision.ProvisionExecutor;

import java.util.UUID;

public class ProvisioningModularDrmCallback {

    private final StreamingModularDrm streamingModularDrm;
    private final ProvisionExecutor provisionExecutor;

    public ProvisioningModularDrmCallback(StreamingModularDrm streamingModularDrm, ProvisionExecutor provisionExecutor) {
        this.streamingModularDrm = streamingModularDrm;
        this.provisionExecutor = provisionExecutor;
    }

    public byte[] executeProvisionRequest(UUID uuid, MediaDrm.ProvisionRequest request) throws Exception {
        return provisionExecutor.execute(request);
    }

    public byte[] executeKeyRequest(UUID uuid, MediaDrm.KeyRequest request) throws Exception {
        return streamingModularDrm.executeKeyRequest(request);
    }
}
