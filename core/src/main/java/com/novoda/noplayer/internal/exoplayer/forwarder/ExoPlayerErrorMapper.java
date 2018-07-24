package com.novoda.noplayer.internal.exoplayer.forwarder;

import android.media.MediaCodec;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ParserException;
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
import com.google.android.exoplayer2.metadata.MetadataDecoderException;
import com.google.android.exoplayer2.offline.DownloadException;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashManifestStaleException;
import com.google.android.exoplayer2.source.hls.SampleQueueMappingException;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.novoda.noplayer.NoPlayer;
import com.novoda.noplayer.NoPlayerError;
import com.novoda.noplayer.PlayerErrorType;

import java.io.IOException;

import static com.novoda.noplayer.internal.exoplayer.forwarder.ErrorFormatter.formatMessage;

final class ExoPlayerErrorMapper {

    private ExoPlayerErrorMapper() {
        // Static class.
    }

    static NoPlayer.PlayerError errorFor(ExoPlaybackException exception) {
        String message = formatMessage(exception.getCause());

        switch (exception.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                return mapSourceError(exception.getSourceException(), message);
            case ExoPlaybackException.TYPE_RENDERER:
                return mapRendererError(exception.getRendererException(), message);
            case ExoPlaybackException.TYPE_UNEXPECTED:
                return mapUnexpectedError(exception.getUnexpectedException(), message);
            default:
                return new NoPlayerError(PlayerErrorType.UNKNOWN, formatMessage(exception.getCause()));
        }
    }

    private static NoPlayer.PlayerError mapSourceError(IOException sourceException, String message) {
        if (sourceException instanceof SampleQueueMappingException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_SAMPLE_QUEUE_MAPPING_ERROR, message);
        }

        if (sourceException instanceof FileDataSource.FileDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READING_LOCAL_FILE_ERROR, message);
        }

        if (sourceException instanceof Loader.UnexpectedLoaderException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_UNEXPECTED_LOADING_ERROR, message);
        }

        if (sourceException instanceof DashManifestStaleException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_LIVE_STALE_MANIFEST_AND_NEW_MANIFEST_COULD_NOT_LOAD_ERROR, message);
        }

        if (sourceException instanceof DownloadException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_DOWNLOAD_ERROR, message);
        }

        if (sourceException instanceof AdsMediaSource.AdLoadException) {
            AdsMediaSource.AdLoadException adLoadException = (AdsMediaSource.AdLoadException) sourceException;
            switch (adLoadException.type) {
                case AdsMediaSource.AdLoadException.TYPE_AD:
                    return new NoPlayerError(PlayerErrorType.SOURCE_AD_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_AD_GROUP:
                    return new NoPlayerError(PlayerErrorType.SOURCE_AD_GROUP_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_ALL_ADS:
                    return new NoPlayerError(PlayerErrorType.SOURCE_ALL_ADS_LOAD_ERROR_THEN_WILL_SKIP, message);
                case AdsMediaSource.AdLoadException.TYPE_UNEXPECTED:
                    return new NoPlayerError(PlayerErrorType.SOURCE_ADS_LOAD_UNEXPECTED_ERROR_THEN_WILL_SKIP, message);
                default:
                    return new NoPlayerError(PlayerErrorType.SOURCE_ADS_LOAD_UNKNOWN_ERROR, message);
            }
        }

        if (sourceException instanceof MergingMediaSource.IllegalMergeException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_MERGING_MEDIA_SOURCE_CANNOT_MERGE_ITS_SOURCES, message);
        }

        if (sourceException instanceof ClippingMediaSource.IllegalClippingException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_CLIPPING_MEDIA_SOURCE_CANNOT_CLIP_WRAPPED_SOURCE, message);
        }

        if (sourceException instanceof PriorityTaskManager.PriorityTooLowException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_TASK_CANNOT_PROCEED_PRIORITY_TOO_LOW, message);
        }

        if (sourceException instanceof ParserException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_PARSING_MEDIA_DATA_OR_METADATA_ERROR, message);
        }

        if (sourceException instanceof Cache.CacheException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_CACHE_WRITING_DATA_ERROR, message);
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistStuckException) {
            HlsPlaylistTracker.PlaylistStuckException playlistStuckException = (HlsPlaylistTracker.PlaylistStuckException) sourceException;
            return new NoPlayerError(PlayerErrorType.SOURCE_HLS_PLAYLIST_STUCK_SERVER_SIDE_ERROR, playlistStuckException.url + " - " + message);
        }

        if (sourceException instanceof HlsPlaylistTracker.PlaylistResetException) {
            HlsPlaylistTracker.PlaylistResetException playlistStuckException = (HlsPlaylistTracker.PlaylistResetException) sourceException;
            return new NoPlayerError(PlayerErrorType.SOURCE_HLS_PLAYLIST_SERVER_HAS_RESET, playlistStuckException.url + " - " + message);
        }

        if (sourceException instanceof HttpDataSource.HttpDataSourceException) {
            HttpDataSource.HttpDataSourceException httpDataSourceException = (HttpDataSource.HttpDataSourceException) sourceException;
            switch (httpDataSourceException.type) {
                case HttpDataSource.HttpDataSourceException.TYPE_OPEN:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_OPEN_ERROR, message);
                case HttpDataSource.HttpDataSourceException.TYPE_READ:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_READ_ERROR, message);
                case HttpDataSource.HttpDataSourceException.TYPE_CLOSE:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_CANNOT_CLOSE_ERROR, message);
                default:
                    return new NoPlayerError(PlayerErrorType.SOURCE_HTTP_UNKNOWN_ERROR, message);
            }
        }

        if (sourceException instanceof AssetDataSource.AssetDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_LOCAL_ASSET_ERROR, message);
        }

        if (sourceException instanceof ContentDataSource.ContentDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_CONTENT_URI_ERROR, message);
        }

        if (sourceException instanceof UdpDataSource.UdpDataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_READ_FROM_UDP_ERROR, message);
        }

        if (sourceException instanceof DataSourceException) {
            return new NoPlayerError(PlayerErrorType.SOURCE_DATA_POSITION_OUT_OF_RANGE_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.SOURCE_UNKNOWN_ERROR, message);

    }

    private static NoPlayer.PlayerError mapRendererError(Exception rendererException, String message) {
        if (rendererException instanceof AudioSink.ConfigurationException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_AUDIO_SINK_CONFIGURATION_ERROR, message);
        }

        if (rendererException instanceof AudioSink.InitializationException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_AUDIO_SINK_INITIALISATION_ERROR, message);
        }

        if (rendererException instanceof AudioSink.WriteException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_AUDIO_SINK_WRITE_ERROR, message);
        }

        if (rendererException instanceof AudioProcessor.UnhandledFormatException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_AUDIO_UNHANDLED_FORMAT_ERROR, message);
        }

        if (rendererException instanceof AudioDecoderException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_AUDIO_DECODER_ERROR, message);
        }

        if (rendererException instanceof MediaCodecRenderer.DecoderInitializationException) {
            MediaCodecRenderer.DecoderInitializationException decoderInitializationException = (MediaCodecRenderer.DecoderInitializationException) rendererException;
            String fullMessage = "decoder-name:" + decoderInitializationException.decoderName + ", "
                    + "mimetype:" + decoderInitializationException.mimeType + ", "
                    + "secureCodeRequired:" + decoderInitializationException.secureDecoderRequired + ", "
                    + "diagnosticInfo:" + decoderInitializationException.diagnosticInfo + ", "
                    + "exceptionMessage:" + message;
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODER_INITIALISATION_ERROR, fullMessage);
        }

        if (rendererException instanceof MediaCodecUtil.DecoderQueryException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_MEDIA_CAPABILITIES_REQUEST_ERROR, message);
        }

        if (rendererException instanceof MetadataDecoderException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODING_METADATA_ERROR, message);
        }

        if (rendererException instanceof SubtitleDecoderException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DECODING_SUBTITLE_ERROR, message);
        }

        if (rendererException instanceof UnsupportedDrmException) {
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) rendererException;
            switch (unsupportedDrmException.reason) {
                case UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME:
                    return new NoPlayerError(PlayerErrorType.RENDERER_UNSUPPORTED_DRM_SCHEME_ERROR, message);
                case UnsupportedDrmException.REASON_INSTANTIATION_ERROR:
                    return new NoPlayerError(PlayerErrorType.RENDERER_DRM_INSTANTIATION_ERROR, message);
                default:
                    return new NoPlayerError(PlayerErrorType.RENDERER_DRM_UNKNOWN_ERROR, message);
            }
        }

        if (rendererException instanceof DefaultDrmSessionManager.MissingSchemeDataException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_CANNOT_ACQUIRE_DRM_SESSION_MISSING_SCHEME_FOR_REQUIRED_UUID_ERROR, message);
        }

        if (rendererException instanceof DrmSession.DrmSessionException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DRM_SESSION_ERROR, message);
        }

        if (rendererException instanceof KeysExpiredException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_DRM_KEYS_EXPIRED_ERROR, message);
        }

        if (rendererException instanceof DecryptionException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_FAIL_DECRYPT_DATA_DUE_NON_PLATFORM_COMPONENT_ERROR, message);
        }

        if (rendererException instanceof MediaCodec.CryptoException) {
            MediaCodec.CryptoException cryptoException = (MediaCodec.CryptoException) rendererException;
            switch (cryptoException.getErrorCode()) {
                case MediaCodec.CryptoException.ERROR_INSUFFICIENT_OUTPUT_PROTECTION:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_INSUFFICIENT_OUTPUT_PROTECTION_ERROR, message);
                case MediaCodec.CryptoException.ERROR_KEY_EXPIRED:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_KEY_EXPIRED_ERROR, message);
                case MediaCodec.CryptoException.ERROR_NO_KEY:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_KEY_NOT_FOUND_WHEN_DECRYPTION_ERROR, message);
                case MediaCodec.CryptoException.ERROR_RESOURCE_BUSY:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_RESOURCE_BUSY_ERROR_THEN_SHOULD_RETRY, message);
                case MediaCodec.CryptoException.ERROR_SESSION_NOT_OPENED:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_DECRYPTION_ATTEMPTED_ON_CLOSED_SEDDION_ERROR, message);
                case MediaCodec.CryptoException.ERROR_UNSUPPORTED_OPERATION:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_LICENSE_POLICY_REQUIRED_NOT_SUPPORTED_BY_DEVICE_ERROR, message);
                default:
                    return new NoPlayerError(PlayerErrorType.RENDERER_CRYPTO_UNKNOWN_ERROR, message);
            }
        }

        if (rendererException instanceof IllegalStateException) {
            return new NoPlayerError(PlayerErrorType.RENDERER_MEDIA_REQUIRES_DRM_SESSION_MANAGER_ERROR, message);
        }

        return new NoPlayerError(PlayerErrorType.RENDERER_UNKNOWN_ERROR, message);
    }

    private static NoPlayer.PlayerError mapUnexpectedError(RuntimeException unexpectedException, String message) {
        return null;
    }
}
