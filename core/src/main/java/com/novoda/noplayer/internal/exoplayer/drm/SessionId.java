package com.novoda.noplayer.internal.exoplayer.drm;

import java.util.Arrays;

final class SessionId {

    private final byte[] sessionIdBytes;

    static SessionId absent() {
        return new SessionId(new byte[0]);
    }

    static SessionId of(byte[] sessionId) {
        return new SessionId(Arrays.copyOf(sessionId, sessionId.length));
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")  // This can only come from the factory methods, which do defensive copying
    private SessionId(byte[] sessionIdBytes) {
        this.sessionIdBytes = sessionIdBytes;
    }

    byte[] asBytes() {
        return Arrays.copyOf(sessionIdBytes, sessionIdBytes.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionId sessionId = (SessionId) o;

        return Arrays.equals(sessionIdBytes, sessionId.sessionIdBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sessionIdBytes);
    }

    @Override
    public String toString() {
        return "SessionId{"
                + "asBytes=" + Arrays.toString(sessionIdBytes)
                + '}';
    }
}
