package com.novoda.noplayer.drm;

import android.annotation.TargetApi;
import android.media.MediaCryptoException;
import android.media.MediaDrmException;
import android.os.Build;
import android.os.Looper;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class LocalDrmSession implements DrmSessionManager<FrameworkMediaCrypto>, DrmSession<FrameworkMediaCrypto> {

    private static final UUID WIDEVINE_MODULAR_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    private final ExoMediaDrm<FrameworkMediaCrypto> mediaDrm;
    private final byte[] keySetIdToRestore;

    private int state = STATE_CLOSED;
    private byte[] sessionId;
    private FrameworkMediaCrypto mediaCrypto;
    private Exception lastKnownException;

    LocalDrmSession(byte[] keySetIdToRestore) throws UnsupportedDrmException {
        this.keySetIdToRestore = Arrays.copyOf(keySetIdToRestore, keySetIdToRestore.length);
        this.mediaDrm = FrameworkMediaDrm.newInstance(WIDEVINE_MODULAR_UUID);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public FrameworkMediaCrypto getMediaCrypto() {
        if (state != STATE_OPENED && state != STATE_OPENED_WITH_KEYS) {
            throw new IllegalStateException();
        }
        return mediaCrypto;
    }

    @Override
    public boolean requiresSecureDecoderComponent(String mimeType) {
        if (state != STATE_OPENED && state != STATE_OPENED_WITH_KEYS) {
            throw new IllegalStateException();
        }
        return mediaCrypto.requiresSecureDecoderComponent(mimeType);
    }

    @Override
    public DrmSessionException getError() {
        return new DrmSessionException(lastKnownException);
    }

    @Override
    public Map<String, String> queryKeyStatus() {
        return null;
    }

    @Override
    public byte[] getOfflineLicenseKeySetId() {
        return keySetIdToRestore;
    }

    @Override
    public DrmSession<FrameworkMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        try {
            state = STATE_OPENING;
            sessionId = mediaDrm.openSession();
            mediaCrypto = mediaDrm.createMediaCrypto(WIDEVINE_MODULAR_UUID, sessionId);
            state = STATE_OPENED;

            mediaDrm.restoreKeys(sessionId, keySetIdToRestore);
            state = STATE_OPENED_WITH_KEYS;
        } catch (MediaDrmException | MediaCryptoException e) {
            onError(e);
        }
        return this;
    }

    @Override
    public void releaseSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        if (sessionId != null) {
            mediaDrm.closeSession(sessionId);
            sessionId = null;
        }
    }

    private void onError(Exception e) {
        // TODO add listener and callback with exception see DefaultDrmSessionManager onError
        state = STATE_ERROR;
        lastKnownException = e;
    }
}
