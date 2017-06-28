package com.novoda.noplayer.internal.drm.provision;

public class ProvisionExecutorCreator {

    public ProvisionExecutor create() {
        HttpUrlConnectionPoster httpPoster = new HttpUrlConnectionPoster();
        ProvisioningCapabilities capabilities = ProvisioningCapabilities.newInstance();
        return new HttpPostingProvisionExecutor(httpPoster, capabilities);
    }
}
