package com.novoda.noplayer.model;

import com.google.android.exoplayer2.RendererCapabilities;

public enum RendererSupport {
    FORMAT_HANDLED(RendererCapabilities.FORMAT_HANDLED),
    FORMAT_EXCEEDS_CAPABILITIES(RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES),
    FORMAT_UNSUPPORTED_DRM(RendererCapabilities.FORMAT_UNSUPPORTED_DRM),
    FORMAT_UNSUPPORTED_SUBTYPE(RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE),
    FORMAT_UNSUPPORTED_TYPE(RendererCapabilities.FORMAT_UNSUPPORTED_TYPE),
    FORMAT_UNKNOWN(-1);

    private final int rawValue;

    RendererSupport(int rawValue) {
        this.rawValue = rawValue;
    }

    public static RendererSupport from(int rawValue) {
        for (RendererSupport rendererCapability : values()) {
            if (rendererCapability.rawValue == rawValue) {
                return rendererCapability;
            }
        }

        return FORMAT_UNKNOWN;
    }
 }
