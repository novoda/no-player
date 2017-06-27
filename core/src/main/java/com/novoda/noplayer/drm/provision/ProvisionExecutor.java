package com.novoda.noplayer.drm.provision;

import com.novoda.noplayer.drm.ModularDrmProvisionRequest;

import java.io.IOException;
import java.nio.charset.Charset;

public class ProvisionExecutor {

    private static final String PARAMETER_SIGNED_REQUEST = "&signedRequest=";

    private final HttpUrlConnectionPoster httpUrlConnectionPoster;
    private final ProvisioningCapabilities capabilities;

    public ProvisionExecutor(HttpUrlConnectionPoster httpUrlConnectionPoster, ProvisioningCapabilities capabilities) {
        this.httpUrlConnectionPoster = httpUrlConnectionPoster;
        this.capabilities = capabilities;
    }

    public byte[] execute(ModularDrmProvisionRequest request) throws IOException, UnableToProvisionException {
        if (isIncapableOfProvisioning()) {
            throw new UnableToProvisionException();
        }
        String provisioningUrl = buildProvisioningUrl(request);
        return httpUrlConnectionPoster.post(provisioningUrl);
    }

    private boolean isIncapableOfProvisioning() {
        return !capabilities.canProvision();
    }

    private String buildProvisioningUrl(ModularDrmProvisionRequest request) {
        return request.url() + PARAMETER_SIGNED_REQUEST + new String(request.data(), Charset.forName("UTF-8"));
    }
}
