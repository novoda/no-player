package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;

import java.util.UUID;

class ProvisioningModularDrmCallback implements MediaDrmCallback {

    private final StreamingModularDrm streamingModularDrm;
    private final ProvisionExecutor provisionExecutor;

    ProvisioningModularDrmCallback(StreamingModularDrm streamingModularDrm, ProvisionExecutor provisionExecutor) {
        this.streamingModularDrm = streamingModularDrm;
        this.provisionExecutor = provisionExecutor;
    }

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws Exception {
        return provisionExecutor.execute(new ModularDrmProvisionRequest(request.getDefaultUrl(), request.getData()));
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        return streamingModularDrm.executeKeyRequest(new ModularDrmKeyRequest(request.getDefaultUrl(), request.getData()));
    }
}
