package com.novoda.noplayer.player;

import com.novoda.noplayer.drm.DrmType;

interface PlayerCapabilities {

    boolean supports(DrmType drmType);

}
