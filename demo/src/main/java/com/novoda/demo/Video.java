package com.novoda.demo;

import android.net.Uri;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DrmType;

public class Video {

    private final String humanReadableRepresentation;
    private final Uri videoUri;
    private final ContentType contentType;
    private final DrmType drmType;
    private final DrmHandler drmHandler;

    public static Video from(String humanReadableRepresentation, String rawVideoUri, ContentType contentType) {
        Uri videoUri = Uri.parse(rawVideoUri);
        return new Video(humanReadableRepresentation, videoUri, contentType, DrmType.NONE, DrmHandler.NO_DRM);
    }

    public static Video from(String humanReadableRepresentation, String rawVideoUri, ContentType contentType, DrmType drmType, DrmHandler drmHandler) {
        Uri videoUri = Uri.parse(rawVideoUri);
        return new Video(humanReadableRepresentation, videoUri, contentType, drmType, drmHandler);
    }

    private Video(String humanReadableRepresentation, Uri videoUri, ContentType contentType, DrmType drmType, DrmHandler drmHandler) {
        this.humanReadableRepresentation = humanReadableRepresentation;
        this.videoUri = videoUri;
        this.contentType = contentType;
        this.drmType = drmType;
        this.drmHandler = drmHandler;
    }

    public String humanReadableRepresentation() {
        return humanReadableRepresentation;
    }

    public Uri videoUri() {
        return videoUri;
    }

    public ContentType contentType() {
        return contentType;
    }

    public DrmType drmType() {
        return drmType;
    }

    public DrmHandler drmHandler() {
        return drmHandler;
    }
}
