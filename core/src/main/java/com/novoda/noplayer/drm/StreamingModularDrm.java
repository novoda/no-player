package com.novoda.noplayer.drm;

import com.novoda.noplayer.drm.provision.ModularDrmKeyRequest;

public interface StreamingModularDrm extends DrmHandler {

    byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException;
}
