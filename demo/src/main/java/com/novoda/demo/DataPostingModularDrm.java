package com.novoda.demo;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.novoda.noplayer.drm.DrmRequestException;
import com.novoda.noplayer.drm.StreamingModularDrm;
import com.novoda.noplayer.drm.provision.ModularDrmKeyRequest;

import java.io.IOException;

class DataPostingModularDrm implements StreamingModularDrm {

    private final String url;

    DataPostingModularDrm(String url) {
        this.url = url;
    }

    @Override
    public byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException {
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("user-agent");
        try {
            return executePost(dataSourceFactory, url, request.data());
        } catch (IOException e) {
            throw DrmRequestException.from(e);
        }
    }

    private byte[] executePost(HttpDataSource.Factory dataSourceFactory, String url, byte[] data) throws IOException {
        HttpDataSource dataSource = dataSourceFactory.createDataSource();
        DataSpec dataSpec = new DataSpec(Uri.parse(url), data, 0, 0, C.LENGTH_UNSET, null, DataSpec.FLAG_ALLOW_GZIP);
        DataSourceInputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);
        try {
            return Util.toByteArray(inputStream);
        } finally {
            Util.closeQuietly(inputStream);
        }
    }
}
