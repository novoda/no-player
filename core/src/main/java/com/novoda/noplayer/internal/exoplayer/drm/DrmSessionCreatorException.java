package com.novoda.noplayer.internal.exoplayer.drm;

import com.novoda.noplayer.drm.DrmType;

public final class DrmSessionCreatorException extends Exception {

    static DrmSessionCreatorException noDrmHandlerFor(DrmType drmType) {
        return new DrmSessionCreatorException("No DrmHandler for DrmType: " + drmType);
    }

    private DrmSessionCreatorException(String message) {
        super(message);
    }
}
