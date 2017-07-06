package com.novoda.noplayer;

import com.novoda.noplayer.drm.DrmType;

import java.util.Arrays;
import java.util.List;

class AndroidMediaPlayerCapabilities implements PlayerCapabilities {

    private static final List<DrmType> SUPPORTED_DRM_TYPES = Arrays.asList(DrmType.NONE, DrmType.WIDEVINE_CLASSIC);

    @Override
    public boolean supports(DrmType drmType) {
        return SUPPORTED_DRM_TYPES.contains(drmType);
    }

}
