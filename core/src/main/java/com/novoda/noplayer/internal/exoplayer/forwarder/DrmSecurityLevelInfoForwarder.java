package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.DrmSecurityLevel;
import com.novoda.noplayer.internal.exoplayer.drm.DrmSecurityLevelEventListener;

import java.util.Collections;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Methods;
import static com.novoda.noplayer.internal.exoplayer.forwarder.ForwarderInformation.Parameters;

class DrmSecurityLevelInfoForwarder implements DrmSecurityLevelEventListener {

    private final NoPlayer.InfoListener infoListener;

    DrmSecurityLevelInfoForwarder(NoPlayer.InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    @Override
    public void contentSecurityLevel(DrmSecurityLevel securityLevel) {
        infoListener.onNewInfo(Methods.CONTENT_SECURITY_LEVEL, Collections.singletonMap(Parameters.SECURITY_LEVEL, securityLevel));
    }

}
