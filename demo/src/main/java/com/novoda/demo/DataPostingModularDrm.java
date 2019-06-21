package com.novoda.demo;

import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.KeyRequestExecutor;

class DataPostingModularDrm implements KeyRequestExecutor {

    private final String url;

    DataPostingModularDrm(String url) {
        this.url = url;
    }

    @Override
    public byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException {
        return HttpClient.post(url, request.data());
    }
}
