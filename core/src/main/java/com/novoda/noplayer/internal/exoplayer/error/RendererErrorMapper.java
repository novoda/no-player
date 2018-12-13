package com.novoda.noplayer.internal.exoplayer.error;

import android.media.MediaCodec;

import com.google.android.exoplayer2.audio.AudioDecoderException;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.DecryptionException;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.KeysExpiredException;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.novoda.noplayer.DetailErrorType;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

final class RendererErrorMapper {

    private RendererErrorMapper() {
        // non-instantiable class
    }

    @SuppressWarnings({"PMD.StdCyclomaticComplexity", "PMD.CyclomaticComplexity", "PMD.ModifiedCyclomaticComplexity", "PMD.NPathComplexity"})
    static NoPlayer.PlayerError map(Exception rendererException, String message) {
        if (rendererException instanceof AudioSink.ConfigurationException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.AUDIO_SINK_CONFIGURATION_ERROR, message);
        }

        if (rendererException instanceof AudioSink.InitializationException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.AUDIO_SINK_INITIALISATION_ERROR, message);
        }

        if (rendererException instanceof AudioSink.WriteException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.AUDIO_SINK_WRITE_ERROR, message);
        }

        if (rendererException instanceof AudioProcessor.UnhandledFormatException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.AUDIO_UNHANDLED_FORMAT_ERROR, message);
        }

        if (rendererException instanceof AudioDecoderException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.AUDIO_DECODER_ERROR, message);
        }

        if (rendererException instanceof MediaCodecRenderer.DecoderInitializationException) {
            MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                    (MediaCodecRenderer.DecoderInitializationException) rendererException;
            String fullMessage = "decoder-name:" + decoderInitializationException.decoderName + ", "
                    + "mimetype:" + decoderInitializationException.mimeType + ", "
                    + "secureCodeRequired:" + decoderInitializationException.secureDecoderRequired + ", "
                    + "diagnosticInfo:" + decoderInitializationException.diagnosticInfo + ", "
                    + "exceptionMessage:" + message;
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.INITIALISATION_ERROR, fullMessage);
        }

        if (rendererException instanceof MediaCodecUtil.DecoderQueryException) {
            return new NoPlayerError(PlayerErrorType.DEVICE_MEDIA_CAPABILITIES, DetailErrorType.UNKNOWN, message);
        }

        if (rendererException instanceof SubtitleDecoderException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER, DetailErrorType.DECODING_SUBTITLE_ERROR, message);
        }

        if (rendererException instanceof UnsupportedDrmException) {
            return mapUnsupportedDrmException((UnsupportedDrmException) rendererException, message);
        }

        if (rendererException instanceof DefaultDrmSessionManager.MissingSchemeDataException) {
            return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.CANNOT_ACQUIRE_DRM_SESSION_MISSING_SCHEME_FOR_REQUIRED_UUID_ERROR, message);
        }

        if (rendererException instanceof DrmSession.DrmSessionException) {
            return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.DRM_SESSION_ERROR, message);
        }

        if (rendererException instanceof KeysExpiredException) {
            return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.DRM_KEYS_EXPIRED_ERROR, message);
        }

        if (rendererException instanceof DecryptionException) {
            return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR, message);
        }

        if (rendererException instanceof MediaCodec.CryptoException) {
            return mapCryptoException((MediaCodec.CryptoException) rendererException, message);
        }

        if (rendererException instanceof IllegalStateException) {
            return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.UNKNOWN, DetailErrorType.UNKNOWN, message);
    }

    private static NoPlayer.PlayerError mapUnsupportedDrmException(UnsupportedDrmException unsupportedDrmException, String message) {
        switch (unsupportedDrmException.reason) {
            case UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.UNSUPPORTED_DRM_SCHEME_ERROR, message);
            case UnsupportedDrmException.REASON_INSTANTIATION_ERROR:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.DRM_INSTANTIATION_ERROR, message);
            default:
                return new NoPlayerError(PlayerErrorType.DRM, DetailErrorType.DRM_UNKNOWN_ERROR, message);
        }
    }

    private static NoPlayer.PlayerError mapCryptoException(MediaCodec.CryptoException cryptoException, String message) {
        switch (cryptoException.getErrorCode()) {
            case MediaCodec.CryptoException.ERROR_INSUFFICIENT_OUTPUT_PROTECTION:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.INSUFFICIENT_OUTPUT_PROTECTION_ERROR, message);
            case MediaCodec.CryptoException.ERROR_KEY_EXPIRED:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.KEY_EXPIRED_ERROR, message);
            case MediaCodec.CryptoException.ERROR_NO_KEY:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.KEY_NOT_FOUND_WHEN_DECRYPTION_ERROR, message);
            case MediaCodec.CryptoException.ERROR_RESOURCE_BUSY:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.RESOURCE_BUSY_ERROR_THEN_SHOULD_RETRY, message);
            case MediaCodec.CryptoException.ERROR_SESSION_NOT_OPENED:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.ATTEMPTED_ON_CLOSED_SEDDION_ERROR, message);
            case MediaCodec.CryptoException.ERROR_UNSUPPORTED_OPERATION:
                return new NoPlayerError(
                        PlayerErrorType.CONTENT_DECRYPTION,
                        DetailErrorType.LICENSE_POLICY_REQUIRED_NOT_SUPPORTED_BY_DEVICE_ERROR,
                        message
                );
            default:
                return new NoPlayerError(PlayerErrorType.CONTENT_DECRYPTION, DetailErrorType.UNKNOWN, message);
        }
    }
}
