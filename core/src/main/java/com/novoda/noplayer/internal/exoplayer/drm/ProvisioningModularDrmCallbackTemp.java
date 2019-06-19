package com.novoda.noplayer.internal.exoplayer.drm;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.KeyRequestExecutor;
import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.ModularDrmProvisionRequest;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;

import java.util.UUID;

class ProvisioningModularDrmCallbackTemp implements MediaDrmCallback {

    private final KeyRequestExecutor keyRequestExecutor;
    private final ProvisionExecutor provisionExecutor;

    ProvisioningModularDrmCallbackTemp(KeyRequestExecutor keyRequestExecutor, ProvisionExecutor provisionExecutor) {
        this.keyRequestExecutor = keyRequestExecutor;
        this.provisionExecutor = provisionExecutor;
    }

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws Exception {
        return provisionExecutor.execute(new ModularDrmProvisionRequest(request.getDefaultUrl(), request.getData()));
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        return keyRequestExecutor.executeKeyRequest(new ModularDrmKeyRequest(request.getLicenseServerUrl(), request.getData()));
    }
}
