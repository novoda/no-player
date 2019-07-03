package com.novoda.noplayer.model;

import com.google.android.exoplayer2.RendererCapabilities;

public enum RendererCapability {
    FORMAT_EXCEEDS_CAPABILITIES(RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES),
    FORMAT_UNSUPPORTED_DRM(RendererCapabilities.FORMAT_UNSUPPORTED_DRM),
    FORMAT_UNSUPPORTED_SUBTYPE(RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE),
    FORMAT_UNSUPPORTED_TYPE(RendererCapabilities.FORMAT_UNSUPPORTED_TYPE),
    FORMAT_UNKNOWN(-1);

    private final int rawValue;

    RendererCapability(int rawValue) {
        this.rawValue = rawValue;
    }

    public static RendererCapability from(int rawValue) {
        for (RendererCapability rendererCapability : values()) {
            if (rendererCapability.rawValue == rawValue) {
                return rendererCapability;
            }
        }

        return FORMAT_UNKNOWN;
    }
 }
