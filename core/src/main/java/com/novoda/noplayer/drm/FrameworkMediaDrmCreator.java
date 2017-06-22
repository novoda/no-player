package com.novoda.noplayer.drm;

import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;

import java.util.UUID;

public class FrameworkMediaDrmCreator {

    public FrameworkMediaDrm create(UUID uuid) {
        try {
            return FrameworkMediaDrm.newInstance(uuid);
        } catch (UnsupportedDrmException e) {
            throw new FrameworkMediaDrmException(e.getMessage(), e.getCause());
        }
    }

    private class FrameworkMediaDrmException extends RuntimeException {

        FrameworkMediaDrmException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
