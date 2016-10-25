package com.novoda.noplayer.drm.provision;

import android.annotation.TargetApi;
import android.media.MediaDrm;
import android.os.Build;

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

    public byte[] execute(MediaDrm.ProvisionRequest request) throws IOException, UnableToProvisionException {
        if (isIncapabaleOfProvisioning()) {
            throw new UnableToProvisionException();
        }
        String provisioningUrl = buildProvisioningUrl(request);
        return httpPoster.post(provisioningUrl);
    }

    private boolean isIncapabaleOfProvisioning() {
        return !capabilities.canProvision();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String buildProvisioningUrl(MediaDrm.ProvisionRequest request) {
        return request.getDefaultUrl() + PARAMETER_SIGNED_REQUEST + new String(request.getData(), Charset.forName("UTF-8"));
    }
}
