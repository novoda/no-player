package com.novoda.noplayer.drm;

@SuppressWarnings({
        "checkstyle:interfaceistype",
        "PMD.AvoidConstantsInterface",
        "PMD.ConstantsInInterface"
}) // This is to allow for multiple different types of DRM
public interface DrmHandler {

    DrmHandler NO_DRM = new DrmHandler() {
    };
}
