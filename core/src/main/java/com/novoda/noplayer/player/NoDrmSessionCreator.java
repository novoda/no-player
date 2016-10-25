package com.novoda.noplayer.player;

import com.google.android.exoplayer.drm.DrmSessionManager;
import com.novoda.noplayer.exoplayer.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerFacade;

class NoDrmSessionCreator implements DrmSessionCreator {

    private static final DrmSessionManager NO_DRM_SESSION_MANANGER = null;

    @Override
    public DrmSessionManager create(ExoPlayerFacade exoPlayerFacade) {
        return NO_DRM_SESSION_MANANGER;
    }
}
