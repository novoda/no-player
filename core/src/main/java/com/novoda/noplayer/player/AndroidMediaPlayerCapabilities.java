package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmHandler;

class AndroidMediaPlayerCapabilities implements PlayerCapabilities {

    @Override
    public boolean supports(DrmHandler drmHandler) {
        return drmHandler == DrmHandler.NO_DRM;
    }
}
