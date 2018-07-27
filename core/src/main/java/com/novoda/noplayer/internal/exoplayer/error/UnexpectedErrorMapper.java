package com.novoda.noplayer.internal.exoplayer.error;

import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.util.EGLSurfaceTexture;
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

final class UnexpectedErrorMapper {

    private UnexpectedErrorMapper() {
        // non-instantiable class
    }

    static NoPlayer.PlayerError map(RuntimeException unexpectedException, String message) {
        if (unexpectedException instanceof EGLSurfaceTexture.GlException) {
            return new NoPlayerError(PlayerErrorType.UNEXPECTED, DetailErrorType.EGL_OPERATION_ERROR, message);
        }

        if (unexpectedException instanceof DefaultAudioSink.InvalidAudioTrackTimestampException) {
            return new NoPlayerError(PlayerErrorType.UNEXPECTED, DetailErrorType.SPURIOUS_AUDIO_TRACK_TIMESTAMP_ERROR, message);
        }

        if (unexpectedException instanceof IllegalStateException) {
            return new NoPlayerError(PlayerErrorType.UNEXPECTED, DetailErrorType.MULTIPLE_RENDERER_MEDIA_CLOCK_ENABLED_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, message);
    }
}
