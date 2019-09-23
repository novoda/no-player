package com.novoda.noplayer.internal.exoplayer.drm;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.novoda.noplayer.model.KeySetId;

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

    @Nullable
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalDrmSession that = (LocalDrmSession) o;

        if (mediaCrypto != null ? !mediaCrypto.equals(that.mediaCrypto) : that.mediaCrypto != null) {
            return false;
        }
        if (keySetIdToRestore != null ? !keySetIdToRestore.equals(that.keySetIdToRestore) : that.keySetIdToRestore != null) {
            return false;
        }
        return sessionId != null ? sessionId.equals(that.sessionId) : that.sessionId == null;
    }

    @Override
    public int hashCode() {
        int result = mediaCrypto != null ? mediaCrypto.hashCode() : 0;
        result = 31 * result + (keySetIdToRestore != null ? keySetIdToRestore.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        return result;
    }
}
