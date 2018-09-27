package com.novoda.noplayer.internal.exoplayer.drm;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.ModularDrmProvisionRequest;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.internal.drm.provision.ProvisionExecutor;

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
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request, @Nullable String mediaProvidedLicenseServerUrl) throws Exception {
        String url = request.getDefaultUrl();

        if (TextUtils.isEmpty(url)) {
            url = mediaProvidedLicenseServerUrl;
        }

        return streamingModularDrm.executeKeyRequest(new ModularDrmKeyRequest(url, request.getData()));
    }
}
