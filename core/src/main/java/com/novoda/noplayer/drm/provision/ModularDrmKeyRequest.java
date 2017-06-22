package com.novoda.noplayer.drm.provision;

public class ModularDrmKeyRequest {

    private final String url;
    private final byte[] data;

    public ModularDrmKeyRequest(String url, byte[] data) {
        this.url = url;
        this.data = data;
    }

    public String url() {
        return url;
    }

    public byte[] data() {
        return data;
    }
}
