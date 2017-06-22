package com.novoda.demo;

import com.novoda.noplayer.drm.DrmRequestException;
import com.novoda.noplayer.drm.ModularDrmKeyRequest;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.HttpPoster;

import java.io.IOException;

class DataPostingModularDrm implements StreamingModularDrm {

    private final String url;
    private final HttpPoster httpPoster;

    static DataPostingModularDrm from(String url) {
        HttpPoster httpPoster = new HttpPoster();
        return new DataPostingModularDrm(url, httpPoster);
    }

    private DataPostingModularDrm(String url, HttpPoster httpPoster) {
        this.url = url;
        this.httpPoster = httpPoster;
    }

    @Override
    public byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException {
        try {
            return httpPoster.post(url, request.data());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
