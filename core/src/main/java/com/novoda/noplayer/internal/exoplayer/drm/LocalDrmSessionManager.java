package com.novoda.noplayer.internal.exoplayer.drm;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.model.KeySetId;

import java.util.UUID;

class LocalDrmSessionManager implements DrmSessionManager<FrameworkMediaCrypto> {

    private final KeySetId keySetIdToRestore;
    private final ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    private final DefaultDrmSessionEventListener eventListener;
    private final UUID drmScheme;
    private final Handler handler;

    LocalDrmSessionManager(KeySetId keySetIdToRestore,
                           ExoMediaDrm<FrameworkMediaCrypto> mediaDrm,
                           UUID drmScheme,
                           Handler handler,
                           DefaultDrmSessionEventListener eventListener) {
        this.keySetIdToRestore = keySetIdToRestore;
        this.mediaDrm = mediaDrm;
        this.eventListener = eventListener;
        this.drmScheme = drmScheme;
        this.handler = handler;
    }

    @Override
    public boolean canAcquireSession(DrmInitData drmInitData) {
        DrmInitData.SchemeData schemeData = drmInitData.get(drmScheme);
        return schemeData != null;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException") // We are forced to catch Exception as ResourceBusyException is minSdk 19
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public DrmSession<FrameworkMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        DrmSession<FrameworkMediaCrypto> drmSession;

        try {
            SessionId sessionId = SessionId.of(mediaDrm.openSession());
            FrameworkMediaCrypto mediaCrypto = mediaDrm.createMediaCrypto(sessionId.asBytes());

            mediaDrm.restoreKeys(sessionId.asBytes(), keySetIdToRestore.asBytes());

            drmSession = new LocalDrmSession(mediaCrypto, keySetIdToRestore, sessionId);
        } catch (Exception exception) {
            drmSession = new InvalidDrmSession(new DrmSession.DrmSessionException(exception));
            notifyErrorListener(drmSession);
        }
        return drmSession;
    }

    private void notifyErrorListener(DrmSession<FrameworkMediaCrypto> drmSession) {
        final DrmSession.DrmSessionException error = drmSession.getError();
        handler.post(new Runnable() {
            @Override
            public void run() {
                eventListener.onDrmSessionManagerError(error);
            }
        });
    }

    @Override
    public void releaseSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        FrameworkDrmSession frameworkDrmSession = (FrameworkDrmSession) drmSession;
        SessionId sessionId = frameworkDrmSession.getSessionId();
        mediaDrm.closeSession(sessionId.asBytes());
    }
}
