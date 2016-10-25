package com.novoda.noplayer.drm;

import android.media.MediaDrm;

public interface StreamingModularDrm extends DrmHandler {

    byte[] executeKeyRequest(MediaDrm.KeyRequest request) throws DrmRequestException;
}
