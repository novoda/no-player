package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmHandler;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.drm.StreamingModularDrm;

class ExoPlayerCapabilities implements PlayerCapabilities {

    @Override
    public boolean supports(DrmHandler drmHandler) {
        return drmHandler == DrmHandler.NO_DRM || drmHandler instanceof StreamingModularDrm || drmHandler instanceof DownloadedModularDrm;
    }

}
