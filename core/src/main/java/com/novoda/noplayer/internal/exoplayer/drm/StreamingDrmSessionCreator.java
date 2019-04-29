package com.novoda.noplayer.internal.exoplayer.drm;

import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.novoda.noplayer.DrmSecurityLevel;
import com.novoda.noplayer.internal.utils.Optional;

import java.util.HashMap;

class StreamingDrmSessionCreator implements DrmSessionCreator {

    @SuppressWarnings("PMD.LooseCoupling")  // Unfortunately the DefaultDrmSessionManager takes a HashMap, not a Map
    private static final HashMap<String, String> NO_OPTIONAL_PARAMETERS = null;

    private final MediaDrmCallback mediaDrmCallback;
    private final FrameworkMediaDrmCreator frameworkMediaDrmCreator;
    private final Handler handler;
    private final DrmSecurityLevelFinder drmSecurityLevelFinder;

    StreamingDrmSessionCreator(MediaDrmCallback mediaDrmCallback,
                               FrameworkMediaDrmCreator frameworkMediaDrmCreator,
                               Handler handler,
                               DrmSecurityLevelFinder drmSecurityLevelFinder) {
        this.mediaDrmCallback = mediaDrmCallback;
        this.frameworkMediaDrmCreator = frameworkMediaDrmCreator;
        this.handler = handler;
        this.drmSecurityLevelFinder = drmSecurityLevelFinder;
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> create(DefaultDrmSessionEventListener eventListener,
                                                          final DrmSecurityLevelEventListener drmSecurityLevelEventListener,
                                                          Optional<DrmSecurityLevel> forcedDrmSecurityLevel) {
        FrameworkMediaDrm frameworkMediaDrm = frameworkMediaDrmCreator.create(WIDEVINE_MODULAR_UUID);
        DefaultDrmSessionManager<FrameworkMediaCrypto> defaultDrmSessionManager = new DefaultDrmSessionManager<>(
                WIDEVINE_MODULAR_UUID,
                frameworkMediaDrm,
                mediaDrmCallback,
                NO_OPTIONAL_PARAMETERS
        );
        defaultDrmSessionManager.removeListener(eventListener);
        defaultDrmSessionManager.addListener(handler, eventListener);

        if (forcedDrmSecurityLevel.isPresent()) {
            frameworkMediaDrm.setPropertyString("securityLevel", forcedDrmSecurityLevel.get().toRawValue());
        }

        final DrmSecurityLevel securityLevel = drmSecurityLevelFinder.findSecurityLevel(Optional.<ExoMediaDrm>of(frameworkMediaDrm));
        handler.post(new Runnable() {
            @Override
            public void run() {
                drmSecurityLevelEventListener.contentSecurityLevel(securityLevel);
            }
        });

        return defaultDrmSessionManager;
    }

}
