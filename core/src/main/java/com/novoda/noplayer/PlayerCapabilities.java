package com.novoda.noplayer;

import com.novoda.noplayer.drm.DrmType;

interface PlayerCapabilities {

    boolean supports(DrmType drmType);

}
