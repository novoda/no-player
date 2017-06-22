package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.provision.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.provision.ModularDrmProvisionRequest;
import com.novoda.noplayer.drm.provision.ProvisionExecutor;

import java.util.UUID;

public class ProvisioningModularDrmCallback implements MediaDrmCallback {

    private final StreamingModularDrm streamingModularDrm;
    private final ProvisionExecutor provisionExecutor;

    public ProvisioningModularDrmCallback(StreamingModularDrm streamingModularDrm, ProvisionExecutor provisionExecutor) {
        this.streamingModularDrm = streamingModularDrm;
        this.provisionExecutor = provisionExecutor;
    }

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws Exception {
        ModularDrmProvisionRequest provisionRequest = new ModularDrmProvisionRequest(request.getDefaultUrl(), request.getData());
        return provisionExecutor.execute(provisionRequest);
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        return streamingModularDrm.executeKeyRequest(new ModularDrmKeyRequest(request.getDefaultUrl(), request.getData()));
    }
}
