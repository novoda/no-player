package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class LocalDrmSession implements FrameworkDrmSession {

    private static final DrmSessionException NO_EXCEPTION = null;

    private final int state;
    private final FrameworkMediaCrypto mediaCrypto;
    private final byte[] keySetIdToRestore;
    private final SessionId sessionId;

    LocalDrmSession(int state, FrameworkMediaCrypto mediaCrypto, byte[] keySetIdToRestore, SessionId sessionId) {
        this.state = state;
        this.mediaCrypto = mediaCrypto;
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
        return NO_EXCEPTION;
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
    public SessionId getSessionId() {
        return sessionId;
    }
}
