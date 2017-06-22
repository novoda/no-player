package com.novoda.noplayer.drm.provision;

import java.util.Arrays;

public class ModularDrmProvisionRequest {

    private final String url;
    private final byte[] data;

    public ModularDrmProvisionRequest(String url, byte[] data) {
        this.url = url;
        this.data = data;
    }

    public String url() {
        return url;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModularDrmProvisionRequest that = (ModularDrmProvisionRequest) o;

        if (url != null ? !url.equals(that.url) : that.url != null) {
            return false;
        }
        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "ModularDrmProvisionRequest{" +
                "url='" + url + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
