package com.novoda.noplayer.drm;

public interface StreamingModularDrm extends DrmHandler {

    byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException;
}
