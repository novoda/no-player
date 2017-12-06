package com.novoda.noplayer.model;

import java.util.Arrays;

public final class KeySetId {

    private final byte[] keySetIdBytes;

    public static KeySetId of(byte[] sessionId) {
        return new KeySetId(Arrays.copyOf(sessionId, sessionId.length));
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")  // This array can only come from the factory method which does defensive copy
    private KeySetId(byte[] keySetIdBytes) {
        this.keySetIdBytes = keySetIdBytes;
    }

    public byte[] asBytes() {
        return Arrays.copyOf(keySetIdBytes, keySetIdBytes.length);
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

        return Arrays.equals(keySetIdBytes, sessionId1.keySetIdBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keySetIdBytes);
    }

    @Override
    public String toString() {
        return "KeySetId{keySetIdBytes=" + Arrays.toString(keySetIdBytes) + '}';
    }
}
