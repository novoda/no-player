package com.novoda.noplayer.drm;

import com.novoda.noplayer.model.KeySetId;

public interface DownloadedModularDrm extends DrmHandler {

    KeySetId getKeySetId();
}
