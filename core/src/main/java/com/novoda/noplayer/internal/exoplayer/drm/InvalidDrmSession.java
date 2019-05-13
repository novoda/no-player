package com.novoda.noplayer.internal.exoplayer.drm;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;

import java.util.Map;

class InvalidDrmSession implements FrameworkDrmSession {

    private static final byte[] ABSENT_OFFLINE_LICENSE_KEY_SET_ID = null;

    private final DrmSessionException drmSessionException;

    InvalidDrmSession(DrmSessionException drmSessionException) {
        this.drmSessionException = drmSessionException;
    }

    @Override
    public int getState() {
        return DrmSession.STATE_ERROR;
    }

    @Override
    public FrameworkMediaCrypto getMediaCrypto() {
        throw new IllegalStateException();
    }

    @Override
    public DrmSessionException getError() {
        return drmSessionException;
    }

    @Override
    public Map<String, String> queryKeyStatus() {
        throw new IllegalStateException();
    }

    @SuppressWarnings("PMD.MethodReturnsInternalArray") // We return a constant null array
    @Override
    @Nullable
    public byte[] getOfflineLicenseKeySetId() {
        return ABSENT_OFFLINE_LICENSE_KEY_SET_ID;
    }

    @Override
    public SessionId getSessionId() {
        return SessionId.absent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvalidDrmSession that = (InvalidDrmSession) o;

        return drmSessionException != null ? drmSessionException.equals(that.drmSessionException) : that.drmSessionException == null;
    }

    @Override
    public int hashCode() {
        return drmSessionException != null ? drmSessionException.hashCode() : 0;
    }
}
