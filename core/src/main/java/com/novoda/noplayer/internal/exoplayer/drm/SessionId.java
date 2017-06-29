package com.novoda.noplayer.internal.exoplayer.drm;

import java.util.Arrays;

class SessionId {

    private final byte[] sessionId;

    static SessionId absent() {
        return new SessionId(new byte[0]);
    }

    static SessionId of(byte[] sessionId) {
        return new SessionId(Arrays.copyOf(sessionId, sessionId.length));
    }

    private SessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    byte[] asBytes() {
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

        SessionId sessionId1 = (SessionId) o;

        return Arrays.equals(sessionId, sessionId1.sessionId);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sessionId);
    }

    @Override
    public String toString() {
        return "SessionId{" +
                "asBytes=" + Arrays.toString(sessionId) +
                '}';
    }
}
