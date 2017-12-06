package com.novoda.noplayer.drm;

import java.util.Arrays;

public class ModularDrmKeyRequest {

    private final String url;
    private final byte[] data;

    public ModularDrmKeyRequest(String url, byte[] data) {
        this.url = url;
        this.data = Arrays.copyOf(data, data.length);
    }

    public String url() {
        return url;
    }

    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModularDrmKeyRequest that = (ModularDrmKeyRequest) o;

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
        return "ModularDrmKeyRequest{"
                + "url='" + url + '\''
                + ", data=" + Arrays.toString(data)
                + '}';
    }
}
