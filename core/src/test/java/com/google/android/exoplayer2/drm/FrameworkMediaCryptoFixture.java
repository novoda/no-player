package com.google.android.exoplayer2.drm;

import android.media.MediaCrypto;
import android.media.MediaCryptoException;

import java.util.UUID;

public final class FrameworkMediaCryptoFixture {

    private MediaCrypto mediaCrypto = new MediaCrypto(UUID.randomUUID(), new byte[0]);
    private boolean forceAllowInsecureDecoderComponents = true;

    private FrameworkMediaCryptoFixture() throws MediaCryptoException {
        // Static factory method.
    }

    public static FrameworkMediaCryptoFixture aFrameworkMediaCrypto() throws MediaCryptoException {
        return new FrameworkMediaCryptoFixture();
    }

    public FrameworkMediaCryptoFixture withMediaCrypto(MediaCrypto mediaCrypto) {
        this.mediaCrypto = mediaCrypto;
        return this;
    }

    public FrameworkMediaCryptoFixture withForceAllowInsecureDecoderComponents(boolean forceAllowInsecureDecoderComponents) {
        this.forceAllowInsecureDecoderComponents = forceAllowInsecureDecoderComponents;
        return this;
    }

    public FrameworkMediaCrypto build() {
        return new FrameworkMediaCrypto(mediaCrypto, forceAllowInsecureDecoderComponents);
    }
}
