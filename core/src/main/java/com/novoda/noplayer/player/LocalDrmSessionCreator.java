package com.novoda.noplayer.player;

import android.annotation.TargetApi;
import android.media.MediaDrmException;
import android.os.Build;

import com.google.android.exoplayer.drm.DrmSessionManager;
import com.novoda.noplayer.drm.DownloadedModularDrm;
import com.novoda.noplayer.exoplayer.DrmSessionCreator;
import com.novoda.noplayer.exoplayer.ExoPlayerFacade;
import com.novoda.noplayer.exoplayer.LocalDrmSession;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class LocalDrmSessionCreator implements DrmSessionCreator {

    private final DownloadedModularDrm downloadedModularDrm;

    LocalDrmSessionCreator(DownloadedModularDrm downloadedModularDrm) {
        this.downloadedModularDrm = downloadedModularDrm;
    }

    @Override
    public DrmSessionManager create(ExoPlayerFacade exoPlayerFacade) {
        byte[] keySetIdToRestore = downloadedModularDrm.getKeySetId();
        try {
            return new LocalDrmSession(keySetIdToRestore);
        } catch (MediaDrmException e) {
            throw new DrmSessionManagerCreationException(e);
        }
    }
}
