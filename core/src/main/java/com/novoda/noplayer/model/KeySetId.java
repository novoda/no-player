package com.novoda.noplayer.model;

import java.util.Arrays;

public class KeySetId {

    private final byte[] keySetId;

    public static KeySetId of(byte[] sessionId) {
        return new KeySetId(Arrays.copyOf(sessionId, sessionId.length));
    }

    private KeySetId(byte[] keySetId) {
        this.keySetId = keySetId;
    }

    public byte[] asBytes() {
        return keySetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeySetId sessionId1 = (KeySetId) o;

        return Arrays.equals(keySetId, sessionId1.keySetId);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keySetId);
    }

    @Override
    public String toString() {
        return "KeySetId{" +
                "keySetId=" + Arrays.toString(keySetId) +
                '}';
    }
}
