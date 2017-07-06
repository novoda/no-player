package com.novoda.noplayer.internal.drm.provision;

import com.novoda.noplayer.drm.ModularDrmProvisionRequest;

import java.io.IOException;

public interface ProvisionExecutor {

    byte[] execute(ModularDrmProvisionRequest request) throws IOException, UnableToProvisionException;
}
