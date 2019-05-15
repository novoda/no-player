package com.google.android.exoplayer2.drm;

import java.util.UUID;

public final class FrameworkMediaCryptoFixture {

    private UUID uuid = UUID.randomUUID();
    private byte[] sessionId = new byte[0];
    private boolean forceAllowInsecureDecoderComponents = true;

    private FrameworkMediaCryptoFixture() {
        // Static factory method.
    }

    public static FrameworkMediaCryptoFixture aFrameworkMediaCrypto() {
        return new FrameworkMediaCryptoFixture();
    }

    public FrameworkMediaCryptoFixture withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public FrameworkMediaCryptoFixture withUUID(byte[] sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public FrameworkMediaCryptoFixture withForceAllowInsecureDecoderComponents(boolean forceAllowInsecureDecoderComponents) {
        this.forceAllowInsecureDecoderComponents = forceAllowInsecureDecoderComponents;
        return this;
    }

    public FrameworkMediaCrypto build() {
        return new FrameworkMediaCrypto(uuid, sessionId, forceAllowInsecureDecoderComponents);
    }
}
