package com.novoda.noplayer.drm.provision;

public class ProvisionExecutorCreator {

    public ProvisionExecutor create() {
        HttpUrlConnectionPoster httpPoster = new HttpUrlConnectionPoster();
        ProvisioningCapabilities capabilities = ProvisioningCapabilities.newInstance();
        return new ProvisionExecutorImpl(httpPoster, capabilities);
    }
}
