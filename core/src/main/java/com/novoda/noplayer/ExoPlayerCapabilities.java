package com.novoda.noplayer;

import com.novoda.noplayer.drm.DrmType;

import java.util.Arrays;
import java.util.List;

class ExoPlayerCapabilities implements PlayerCapabilities {

    private static final List<DrmType> SUPPORTED_DRM_TYPES = Arrays.asList(
            DrmType.NONE,
            DrmType.WIDEVINE_MODULAR_STREAM,
            DrmType.WIDEVINE_MODULAR_DOWNLOAD
    );

    @Override
    public boolean supports(DrmType drmType) {
        return SUPPORTED_DRM_TYPES.contains(drmType);
    }
}
