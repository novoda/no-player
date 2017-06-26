package com.novoda.noplayer.exoplayer.drm;

import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.drm.KeySetId;
import com.novoda.noplayer.drm.SessionId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class LocalDrmSession implements FrameworkDrmSession {

    private static final DrmSessionException NO_EXCEPTION = null;

    private final FrameworkMediaCrypto mediaCrypto;
    private final KeySetId keySetIdToRestore;
    private final SessionId sessionId;

    LocalDrmSession(FrameworkMediaCrypto mediaCrypto, KeySetId keySetIdToRestore, SessionId sessionId) {
        this.mediaCrypto = mediaCrypto;
        this.keySetIdToRestore = keySetIdToRestore;
        this.sessionId = sessionId;
    }

    @Override
    public int getState() {
        return STATE_OPENED_WITH_KEYS;
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
        return keySetIdToRestore.asBytes();
    }

    @Override
    public SessionId getSessionId() {
        return sessionId;
    }
}
