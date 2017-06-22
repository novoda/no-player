package com.novoda.noplayer.drm;

import android.annotation.TargetApi;
import android.media.MediaCryptoException;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import android.os.Build;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.UUID;

class LocalDrmSessionManager implements DrmSessionManager<FrameworkMediaCrypto> {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    private final KeySetId keySetIdToRestore;
    private final ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;

    LocalDrmSessionManager(KeySetId keySetIdToRestore, ExoMediaDrm<FrameworkMediaCrypto> mediaDrm) {
        this.keySetIdToRestore = keySetIdToRestore;
        this.mediaDrm = mediaDrm;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public DrmSession<FrameworkMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        DrmSession<FrameworkMediaCrypto> drmSession;

        try {
            SessionId sessionId = SessionId.of(mediaDrm.openSession());
            FrameworkMediaCrypto mediaCrypto = mediaDrm.createMediaCrypto(WIDEVINE_MODULAR_UUID, sessionId.asBytes());

            mediaDrm.restoreKeys(sessionId.asBytes(), keySetIdToRestore.asBytes());

            drmSession = new LocalDrmSession(mediaCrypto, keySetIdToRestore, sessionId);
        } catch (NotProvisionedException | MediaCryptoException | ResourceBusyException e) {
            drmSession = new InvalidDrmSession(new DrmSession.DrmSessionException(e));
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
