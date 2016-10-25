package com.novoda.noplayer.exoplayer;

import com.google.android.exoplayer.drm.DrmSessionManager;

public interface DrmSessionCreator {

    DrmSessionManager create(ExoPlayerFacade exoPlayerFacade);

    class DrmSessionManagerCreationException extends RuntimeException {

        public DrmSessionManagerCreationException(Throwable cause) {
            super(cause);
        }
    }
}
