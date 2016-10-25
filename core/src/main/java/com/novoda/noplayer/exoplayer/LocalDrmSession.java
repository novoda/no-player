package com.novoda.noplayer.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.MediaDrmException;
import android.media.UnsupportedSchemeException;
import android.os.Build;

import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;

import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LocalDrmSession implements DrmSessionManager {

    private final MediaDrm mediaDrm;
    private final byte[] keySetIdToRestore;

    private int state = STATE_CLOSED;
    private byte[] sessionId;
    private MediaCrypto mediaCrypto;
    private Exception lastKnownException;

    public LocalDrmSession(byte[] keySetIdToRestore) throws UnsupportedSchemeException {
        this.keySetIdToRestore = Arrays.copyOf(keySetIdToRestore, keySetIdToRestore.length);
        this.mediaDrm = new MediaDrm(StreamingDrmSessionManager.WIDEVINE_UUID);
    }

    @Override
    public void open(DrmInitData drmInitData) {
        try {
            state = STATE_OPENING;
            sessionId = mediaDrm.openSession();
            mediaCrypto = new MediaCrypto(StreamingDrmSessionManager.WIDEVINE_UUID, sessionId);
            state = STATE_OPENED;

            mediaDrm.restoreKeys(sessionId, keySetIdToRestore);
            state = STATE_OPENED_WITH_KEYS;
        } catch (MediaDrmException | MediaCryptoException e) {
            onError(e);
        }
    }

    @Override
    public void close() {
        if (sessionId != null) {
            mediaDrm.closeSession(sessionId);
            sessionId = null;
        }
    }

    private void onError(Exception e) {
        state = STATE_ERROR;
        lastKnownException = e;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public MediaCrypto getMediaCrypto() {
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
    public Exception getError() {
        return lastKnownException;
    }
}
