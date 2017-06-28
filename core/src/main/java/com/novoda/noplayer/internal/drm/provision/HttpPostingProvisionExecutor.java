package com.novoda.noplayer.internal.drm.provision;

import com.novoda.noplayer.drm.ModularDrmProvisionRequest;

import java.io.IOException;
import java.nio.charset.Charset;

class HttpPostingProvisionExecutor implements ProvisionExecutor {

    private static final String PARAMETER_SIGNED_REQUEST = "&signedRequest=";

    private final HttpUrlConnectionPoster httpPoster;
    private final ProvisioningCapabilities capabilities;

    HttpPostingProvisionExecutor(HttpUrlConnectionPoster httpPoster, ProvisioningCapabilities capabilities) {
        this.httpPoster = httpPoster;
        this.capabilities = capabilities;
    }

    @Override
    public byte[] execute(ModularDrmProvisionRequest request) throws IOException, UnableToProvisionException {
        if (isIncapableOfProvisioning()) {
            throw new UnableToProvisionException();
        }
        String provisioningUrl = buildProvisioningUrl(request);
        return httpPoster.post(provisioningUrl);
    }

    private boolean isIncapableOfProvisioning() {
        return !capabilities.canProvision();
    }

    private String buildProvisioningUrl(ModularDrmProvisionRequest request) {
        return request.url() + PARAMETER_SIGNED_REQUEST + new String(request.data(), Charset.forName("UTF-8"));
    }
}
