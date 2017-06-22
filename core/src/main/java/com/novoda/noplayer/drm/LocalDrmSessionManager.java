package com.novoda.noplayer.drm;

import android.annotation.TargetApi;
import android.media.MediaCryptoException;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import android.os.Build;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.UUID;

class LocalDrmSessionManager implements DrmSessionManager<FrameworkMediaCrypto> {

    private final KeySetId keySetIdToRestore;
    private final ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    private final DefaultDrmSessionManager.EventListener eventListener;
    private final UUID drmScheme;

    LocalDrmSessionManager(KeySetId keySetIdToRestore, ExoMediaDrm<FrameworkMediaCrypto> mediaDrm, DefaultDrmSessionManager.EventListener eventListener, UUID drmScheme) {
        this.keySetIdToRestore = keySetIdToRestore;
        this.mediaDrm = mediaDrm;
        this.eventListener = eventListener;
        this.drmScheme = drmScheme;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public DrmSession<FrameworkMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        DrmSession<FrameworkMediaCrypto> drmSession;

        try {
            SessionId sessionId = SessionId.of(mediaDrm.openSession());
            FrameworkMediaCrypto mediaCrypto = mediaDrm.createMediaCrypto(drmScheme, sessionId.asBytes());

            mediaDrm.restoreKeys(sessionId.asBytes(), keySetIdToRestore.asBytes());

            drmSession = new LocalDrmSession(mediaCrypto, keySetIdToRestore, sessionId);
        } catch (NotProvisionedException | MediaCryptoException | ResourceBusyException exception) {
            drmSession = new InvalidDrmSession(new DrmSession.DrmSessionException(exception));
            eventListener.onDrmSessionManagerError(drmSession.getError());
        }

        return drmSession;
    }

    @Override
    public void releaseSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        FrameworkDrmSession frameworkDrmSession = (FrameworkDrmSession) drmSession;
        SessionId sessionId = frameworkDrmSession.getSessionId();
        mediaDrm.closeSession(sessionId.asBytes());
    }
}
