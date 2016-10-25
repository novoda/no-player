package com.novoda.noplayer.player;

import android.os.Handler;

import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.novoda.noplayer.exoplayer.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerFacade;

class StreamingDrmSessionCreator implements DrmSessionCreator {

    private final MediaDrmCallback drmCallback;
    private final Handler eventHandler = new Handler();

    StreamingDrmSessionCreator(MediaDrmCallback drmCallback) {
        this.drmCallback = drmCallback;
    }

    @Override
    public DrmSessionManager create(ExoPlayerFacade exoPlayerFacade) {
        try {
            return StreamingDrmSessionManager.newWidevineInstance(
                    exoPlayerFacade.getPlaybackLooper(),
                    drmCallback,
                    null,
                    eventHandler,
                    exoPlayerFacade
            );
        } catch (UnsupportedDrmException e) {
            throw new DrmSessionManagerCreationException(e);
        }
    }
}
