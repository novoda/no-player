package com.novoda.noplayer.drm;

public class DrmSessionCreatorFactory {

    public DrmSessionCreator createFor(DrmType drmType, DrmHandler drmHandler) {
        return new NoDrmSessionCreator();
    }
}
