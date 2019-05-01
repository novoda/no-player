package com.novoda.noplayer.internal.exoplayer.error;

import android.media.MediaCodec;
import android.os.Build;

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

        if (unexpectedException instanceof IllegalStateException && message.contains("Multiple renderer media clocks")) {
            return new NoPlayerError(PlayerErrorType.UNEXPECTED, DetailErrorType.MULTIPLE_RENDERER_MEDIA_CLOCK_ENABLED_ERROR, message);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && unexpectedException instanceof MediaCodec.CodecException) {
            String errorMessage = ErrorFormatter.formatCodecException((MediaCodec.CodecException) unexpectedException);
            return new NoPlayerError(PlayerErrorType.UNEXPECTED, DetailErrorType.UNEXPECTED_CODEC_ERROR, errorMessage);
        }

        return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, message);
    }
}
