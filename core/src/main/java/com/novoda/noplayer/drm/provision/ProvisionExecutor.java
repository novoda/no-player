package com.novoda.noplayer.drm.provision;

import com.novoda.noplayer.drm.ModularDrmProvisionRequest;

import java.io.IOException;
import java.nio.charset.Charset;

public class ProvisionExecutor {

    private static final String PARAMETER_SIGNED_REQUEST = "&signedRequest=";

    private final HttpPoster httpPoster;
    private final ProvisioningCapabilities capabilities;

    public static ProvisionExecutor newInstance() {
        HttpPoster httpPoster = new HttpPoster();
        ProvisioningCapabilities capabilities = ProvisioningCapabilities.newInstance();
        return new ProvisionExecutor(httpPoster, capabilities);
    }

    ProvisionExecutor(HttpPoster httpPoster, ProvisioningCapabilities capabilities) {
        this.httpPoster = httpPoster;
        this.capabilities = capabilities;
    }

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
