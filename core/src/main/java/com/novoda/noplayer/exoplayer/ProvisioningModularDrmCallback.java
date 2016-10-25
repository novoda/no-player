package com.novoda.noplayer.exoplayer;

import android.media.MediaDrm;

import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.StreamingModularDrm;
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
    public byte[] executeProvisionRequest(UUID uuid, MediaDrm.ProvisionRequest request) throws Exception {
        return provisionExecutor.execute(request);
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, MediaDrm.KeyRequest request) throws Exception {
        return streamingModularDrm.executeKeyRequest(request);
    }
}
