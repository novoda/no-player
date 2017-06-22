package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class LocalDrmSession implements FrameworkDrmSession {

    private int state;
    private FrameworkMediaCrypto mediaCrypto;
    private Exception lastKnownException;
    private byte[] keySetIdToRestore;
    private final byte[] sessionId;

    LocalDrmSession(int state, FrameworkMediaCrypto mediaCrypto, Exception lastKnownException, byte[] keySetIdToRestore, byte[] sessionId) {
        this.state = state;
        this.mediaCrypto = mediaCrypto;
        this.lastKnownException = lastKnownException;
        this.keySetIdToRestore = keySetIdToRestore;
        this.sessionId = sessionId;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public FrameworkMediaCrypto getMediaCrypto() {
        return mediaCrypto;
    }

    @Override
    public boolean requiresSecureDecoderComponent(String mimeType) {
        return mediaCrypto.requiresSecureDecoderComponent(mimeType);
    }

    @Override
    public DrmSessionException getError() {
        return new DrmSessionException(lastKnownException);
    }

    @Override
    public Map<String, String> queryKeyStatus() {
        return Collections.unmodifiableMap(new HashMap<String, String>());
    }

    @Override
    public byte[] getOfflineLicenseKeySetId() {
        return keySetIdToRestore;
    }

    @Override
    public byte[] getSessionId() {
        return sessionId;
    }
}
