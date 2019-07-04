package com.novoda.noplayer.internal;

import com.google.android.exoplayer2.RendererCapabilities;
import com.novoda.noplayer.model.Support;

public final class SupportMapper {

    private SupportMapper() {
        // Static class
    }

    public static Support from(int rawValue) {
        if (rawValue == RendererCapabilities.FORMAT_HANDLED) {
            return Support.FORMAT_HANDLED;
        }

        if (rawValue == RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES) {
            return Support.FORMAT_EXCEEDS_CAPABILITIES;
        }

        if (rawValue == RendererCapabilities.FORMAT_UNSUPPORTED_DRM) {
            return Support.FORMAT_UNSUPPORTED_DRM;
        }

        if (rawValue == RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE) {
            return Support.FORMAT_UNSUPPORTED_SUBTYPE;
        }

        if (rawValue == RendererCapabilities.FORMAT_UNSUPPORTED_TYPE) {
            return Support.FORMAT_UNSUPPORTED_TYPE;
        }

        return Support.FORMAT_UNKNOWN;
    }
 }
