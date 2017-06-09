package com.novoda.noplayer.drm.provision;

public class ModularDrmKeyRequest {

    private final String url;
    private final byte[] data;

    public ModularDrmKeyRequest(String url, byte[] data) {
        this.url = url;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getData() {
        return data;
    }
}
