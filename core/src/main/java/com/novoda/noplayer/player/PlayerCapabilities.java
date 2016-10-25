package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmHandler;

interface PlayerCapabilities {

    boolean supports(DrmHandler drmHandler);

}
